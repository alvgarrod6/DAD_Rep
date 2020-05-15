#include <Arduino.h>
#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>
#include <PubSubClient.h>

char responseBuffer[300];
WiFiClient espclient;

String SSID = "Dunar"; //nombre de la red a la que el ESP se conectara
String PASS = "d7?a35D9EnaPepXY?c!4"; //contraseña de acceso

char * mqtt_server = "localhost";//Nombre del servidor MQTT
PubSubClient client(espclient);

String SERVER_IP = "192.168.1.104"; //poner direccion ip
int SERVER_PORT = 8080; //poner puerto de escucha

int device = 82664; //poner aquí el id del dispositivos (teoricamente guardado en flash)

void lecturaSensor(int pin); //insert en la bbdd de las lecturas
void regar(int pin, boolean pulsador); //insert en la bbdd de los riegos
int getUmbral(int id); //get del umbral para saber cuando regar

void callback(char* topic, byte* payload, unsigned int length);//funcion que trata los mensajes recibidos desde los canales MQTT
boolean reconnect(void);//Funcion que se encarga de realizar la conexion al serivido MQTT y tambien de reconectarse en caso de desconexion
void clienteMqtt(void);//Funcion que llama los diferentes metodos para publicar informacion en los canales
void topicoSensor(int pin);//funcion que publica informacion en el canal Sensor
void topicoInfo(int pin);//funcion que publica informacion en el canal Info

int valor = 495; //variable que se borrara en la siguiente iteracion

void setup() {
  Serial.begin(9600);

  WiFi.begin(SSID, PASS);

  Serial.print("Connecting...");

  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.print("Connected, IP address: ");
  Serial.print(WiFi.localIP());


  client.setServer(mqtt_server, 1885);//indica el servido MQTT y el puerto
  client.setCallback(callback);//Indica funcion que se encarga de recibir mensajes desde los canales
}

void loop() {
  lecturaSensor(1); //pasarle el numero de pin que esta conectado el sensor al ESP
  clienteMqtt();
  delay(5000);
}

int getUmbral(int id){
  int umbral = -1;
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(espclient, SERVER_IP, SERVER_PORT, "/api/device/sensor/id/"+String(id), true);
    http.GET();

    String payload = http.getString();

    const size_t capacity = JSON_OBJECT_SIZE(6) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc,payload);

    if(error){
      Serial.print("deserializeJson() failed: ");
      Serial.println(error.c_str());
    }
    umbral = doc[0]["umbral"].as<int>();
  }

  return umbral;

}

void lecturaSensor(int pin){
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(espclient, SERVER_IP, SERVER_PORT, "/api/device/sensor/values", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    doc["idsensor"] = pin;
    doc["value"] = valor; //aqui ira en vez de valor, la lectura del sensor
    doc["accuracy"] = 1;
    doc["timestamp"] = 12052020130800; //funcion para calcular timestamp actual

    int umbral = getUmbral(pin);
    delay(1000);

    Serial.println(umbral);

    String output;

    serializeJson(doc, output);

    http.POST(output);

    String payload = http.getString();

    Serial.println("Resultado: lectura realizada");

    if(valor >= umbral){
      regar(pin, false); //false porque no se ha accionado el boton de riego;
      valor = 495;
    }
    valor++;
  }
}

void regar(int pin, boolean pulsador){
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(espclient, SERVER_IP, SERVER_PORT, "/api/device/sensor/riego", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["timestamp"] = 12052020130800;
    doc["humedad"] = valor; //valor leido por el sensor
    doc["manualAuto"] = pulsador; //estado del boton
    doc["idsensor"] = pin;

    String output;
    serializeJson(doc, output);

    http.POST(output);

    String payload = http.getString();

    Serial.println("Resultado: " + payload);
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  if (strcmp(topic,"riego")==0){//Comprueba si se recibe un mensaje desde el topico riego
    Serial.println("Mensaje recibido desde topico Riego");
    StaticJsonDocument<256> doc;
    deserializeJson(doc, payload, length);
    char buffer[512];
    if(valor>=getUmbral(doc["idsensor"].as<int>()))//Si el valordel sensor es mayor al umbral se hace un riego
    {
      regar(doc["idsensor"].as<int>(), true);
      sprintf(buffer,"El riego se ha realizado exitosamente");
    }else{//Si no lo es se envia un mensaje de error
      sprintf(buffer,"Peligro! El riego ha fallado, valor < umbral");
    }
    client.publish("riego", buffer);//publica mensaje en el canal riego
  }
}
boolean reconnect() {//Funcion que se subscribe a los canales
  if (client.connect("Arduino client")) {
    client.subscribe("info");
    Serial.println("conectado a topico Info");
    client.subscribe("sensor");
    Serial.println("conectado a topico Sensor");
    client.subscribe("riego");
    Serial.println("conectado a topico Riego");
    return client.connected();
  }
  Serial.println("Subscripcion fallida");
  return 0;
}

void clienteMqtt(){
  if (!client.connected()) {//Comprueba si estamos conectados al servidor clienteMqtt
    do {
        Serial.print("Connecting ...\n");
    } while(reconnect()==0);
  }
  else {

    topicoSensor(1);//Funcion que publica mensajes en el topico sensor
    delay(1000);

    topicoInfo(1);//Funcion que publica mensajes en el topico info
    delay(1000);
  }


}
void topicoSensor(int pin){//funcion que construye el json que se va a publicar en el canal
  const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
  DynamicJsonDocument doc(capacity);

  doc["idsensor"] = pin;
  doc["value"] = valor; //aqui ira en vez de valor, la lectura del sensor
  doc["accuracy"] = 1;
  doc["timestamp"] = 12052020130800; //funcion para calcular timestamp actual

  delay(1000);

  char buffer[512];
  serializeJson(doc, buffer);
  client.publish("sensor", buffer);//publica el json en el canal
  Serial.println("Se ha publicado en el topico Sensor");
}
void topicoInfo(int pin){//funcion que publica el estado de los sensores

  char buffer[512];
  if(valor!=-1){
    sprintf(buffer,"El sensor con id %d está capturando información",pin);
  }else{
    sprintf(buffer,"Error!!! el sensor con id %d no responde",pin);
  }

  client.publish("info", buffer);//publica el mensaje en el canal
  Serial.println("Se ha publicado en el topico Info");
}
