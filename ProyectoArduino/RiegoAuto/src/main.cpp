#include <Arduino.h>
#include "ArduinoJson.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>

char responseBuffer[300];
WiFiClient client;

String SSID = "Dunar"; //nombre de la red a la que el ESP se conectara
String PASS = "d7?a35D9EnaPepXY?c!4"; //contraseña de acceso

String SERVER_IP = "192.168.1.104"; //poner direccion ip
int SERVER_PORT = 8080; //poner puerto de escucha

int device = 82664; //poner aquí el id del dispositivos (teoricamente guardado en flash)

void lecturaSensor(int pin); //insert en la bbdd de las lecturas
void regar(int pin, boolean pulsador); //insert en la bbdd de los riegos
int getUmbral(int id); //get del umbral para saber cuando regar

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
}

void loop() {
  lecturaSensor(1); //pasarle el numero de pin que esta conectado el sensor al ESP
  delay(5000);
}

int getUmbral(int id){
  int umbral = -1;
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/device/sensor/id/"+String(id), true);
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
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/device/sensor/values", true);
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
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/device/sensor/riego", true);
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
