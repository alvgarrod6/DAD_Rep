#include <Arduino.h>
#include "ArduinoJson.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>
#include "ESPDateTime.h"

char responseBuffer[300];
WiFiClient client;

const int pinSensor = A0;
const int pinBoton = 14;
const int ledRojo = 0;
const int ledAmarillo = 4;
const int ledVerde = 5;
const int bomba = 2;

String SSID = "Dunar"; //nombre de la red a la que el ESP se conectara
String PASS = "d7?a35D9EnaPepXY?c!4"; //contraseña de acceso

String SERVER_IP = "192.168.1.104"; //poner direccion ip
int SERVER_PORT = 8080; //poner puerto de escucha

int device = 82664; //poner aquí el id del dispositivos (teoricamente guardado en flash)
bool boton = false;

void guardarLecturas(int valor); //insert en la bbdd de las lecturas
void regar(boolean pulsador, int valor, int potencia); //insert en la bbdd de los riegos
int getUmbral(); //get del umbral para saber cuando regar
void estadoLEDs(int valor, int umbral);
int getPotencia();
void riegoManual(int valor, int potencia);

void setup() {
  Serial.begin(9600);
  DateTime.begin();

  pinMode(pinSensor, INPUT);
  pinMode(pinBoton, INPUT);

  pinMode(ledRojo, OUTPUT);
  pinMode(ledAmarillo, OUTPUT);
  pinMode(ledVerde, OUTPUT);

  pinMode(bomba, OUTPUT);

  digitalWrite(ledRojo,LOW);
  digitalWrite(ledAmarillo,LOW);
  digitalWrite(ledVerde,LOW);
  digitalWrite(bomba,LOW);

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
  int umbral = getUmbral();
  int potencia = getPotencia();
  int valor = analogRead(pinSensor);
  estadoLEDs(valor,umbral);
  if(valor<1024){
    guardarLecturas(valor);
    if(digitalRead(pinBoton)){
      riegoManual(valor, potencia);
    }else{
      if(valor >= umbral){
        regar(false, valor, potencia);
      }
    }
  }
  delay(10000);
}

void riegoManual(int valor, int potencia){
  if(boton){
    regar(true, valor, potencia);
  }
}

int getUmbral(){
  int umbral = -1;
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/device/sensor/id/"+String(pinSensor), true);
    http.GET();

    String payload = http.getString();

    const size_t capacity = JSON_OBJECT_SIZE(6) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc,payload);

    if(error){
      Serial.print("deserializeJson() failed umbral: ");
      Serial.println(error.c_str());
    }
    umbral = doc[0]["umbral"].as<int>();

  }

  return umbral;

}

int getPotencia(){
  int potencia = -1;
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/device/sensor/id/"+String(pinSensor), true);
    http.GET();

    String payload = http.getString();

    const size_t capacity = JSON_OBJECT_SIZE(6) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc,payload);

    if(error){
      Serial.print("deserializeJson() failed potencia: ");
      Serial.println(error.c_str());
    }
    potencia = doc[0]["potencia"].as<int>();

  }

  return potencia;

}

void guardarLecturas(int valor){
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/device/sensor/values", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    doc["idsensor"] = pinSensor;
    doc["value"] = valor;
    doc["accuracy"] = 1;
    doc["timestamp"] = DateTime.now();

    String output;

    serializeJson(doc, output);

    http.POST(output);

    String payload = http.getString();
  }
}

void regar(boolean pulsador, int valor, int potencia){
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/device/sensor/riego", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["timestamp"] = DateTime.now();
    doc["humedad"] = valor; //valor leido por el sensor
    doc["manualAuto"] = pulsador; //estado del boton
    doc["idsensor"] = pinSensor;

    String output;
    serializeJson(doc, output);

    http.POST(output);

    int tiempo = 30 * potencia;

    digitalWrite(bomba,HIGH);
    delay(tiempo);
    digitalWrite(bomba,LOW);

    String payload = http.getString();
  }
}

void estadoLEDs(int valor, int umbral){
  double fv = (umbral/1.2);
  if(valor >= 1024){
    digitalWrite(ledRojo,HIGH);
    digitalWrite(ledAmarillo,LOW);
    digitalWrite(ledVerde,LOW);
    boton = false;
  }else if(valor>=fv){
    digitalWrite(ledRojo,LOW);
    digitalWrite(ledAmarillo,HIGH);
    digitalWrite(ledVerde,LOW);
    boton = true;
  }else if(valor<fv){
    digitalWrite(ledRojo,LOW);
    digitalWrite(ledAmarillo,LOW);
    digitalWrite(ledVerde,HIGH);
    boton = false;
  }
}
