#include <WiFi.h>
#include <WiFiUdp.h>
#include <ArduinoJson.h>
#include <Wire.h>  // Thư viện Wire cho kết nối I2C
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

#define SERVER_PORT 20001

const char *ssid = "Giangnam Coffee";
const char *password = "";
WiFiUDP udp;

// Định nghĩa kích thước buffer cho JSON document
const int bufferSize = JSON_OBJECT_SIZE(3) + JSON_OBJECT_SIZE(4) + 30; // Thay đổi kích thước buffer tùy theo nhu cầu

DynamicJsonBuffer jsonBuffer(bufferSize); // Khai báo buffer JSON

#define SCREEN_WIDTH 128  // Định nghĩa chiều rộng màn hình OLED
#define SCREEN_HEIGHT 32  // Định nghĩa chiều cao màn hình OLED
#define OLED_RESET -1     // Không sử dụng kết nối reset OLED

Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);

void setup() {

  Serial.begin(9600);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }

  Serial.println("Connected to WiFi: " + WiFi.localIP().toString());
  udp.begin(SERVER_PORT); // Thay đổi cổng UDP thành cổng bạn đã sử dụng trong mã Android

  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
    Serial.println(F("SSD1306 allocation failed"));
    for (;;);
  }
  display.setTextColor(SSD1306_WHITE);
  display.clearDisplay();
}

void loop() {
  int packetSize = udp.parsePacket();
  if (packetSize) {    
    char packetBuffer[packetSize + 1];
    udp.read(packetBuffer, packetSize);
    packetBuffer[packetSize] = '\0';

    Serial.print("Received packet: ");
    Serial.println(packetBuffer);

    JsonObject& root = jsonBuffer.parseObject(packetBuffer);

    if (!root.success()) {
      Serial.println("parseObject() failed");
      return;
    }

    // Trích xuất thông tin từ JSON
    const int type = root["type"];
    
    if(type == 2){      
      const char* deviceName = root["deviceName"];
      const char* seekBarValue = root["seekBarValue"];
      const char* status = root["status"];

      Serial.println("Type: LIGHT");
      Serial.print("Device: ");
      Serial.println(deviceName);
      Serial.print("seekBarValue: ");
      Serial.println(seekBarValue);
      Serial.print("Status: ");
      Serial.println(status);

      // Hiển thị thông tin lên màn hình OLED   
      display.clearDisplay();  
      display.setCursor(0,0);
      display.println("Type:");
      display.setCursor(64,0);
      display.println("Light");
      display.setCursor(0,8);
      display.println("Device: ");
      display.setCursor(64,8);
      display.println(deviceName);
      display.setCursor(0,16);
      display.print("SBValue: ");
      display.setCursor(64,16);
      display.print(seekBarValue);
      display.setCursor(0,24);
      display.print("Status: ");
      display.setCursor(64,24);
      display.print(status);
      display.display();
    }
    else if(type == 1){
      const char* deviceName = root["deviceName"];
      const char* progressValue = root["progressValue"];
      const char* status = root["status"];

      Serial.print("Type: Humidity");
      Serial.print("Device: ");
      Serial.println(deviceName);
      Serial.print("PGValue: ");
      Serial.println(progressValue);
      Serial.print("Status: ");
      Serial.println(status);

      
      // Hiển thị thông tin lên màn hình OLED   
      display.clearDisplay();  
      display.setCursor(0,0);
      display.println("Type:");
      display.setCursor(64,0);
      display.println("Humidity");
      display.setCursor(0,8);
      display.println("Device: ");
      display.setCursor(64,8);
      display.println(deviceName);
      display.setCursor(0,16);
      display.print("PGValue: ");
      display.setCursor(64,16);
      display.print(progressValue);
      display.setCursor(0,24);
      display.print("Status: ");
      display.setCursor(64,24);
      display.print(status);
      display.display();      
    }
    else if(type == 3){
      const char* deviceName = root["deviceName"];
      const char* progressValue = root["progressValue"];
      const char* status = root["status"];

      Serial.print("Type: Temperature");
      Serial.print("Device: ");
      Serial.println(deviceName);
      Serial.print("progressValue: ");
      Serial.println(progressValue);
      Serial.print("Status: ");
      Serial.println(status);

      // Hiển thị thông tin lên màn hình OLED   
      display.clearDisplay();  
      display.setCursor(0,0);
      display.println("Type:");
      display.setCursor(64,0);
      display.println("Temperature");
      display.setCursor(0,8);
      display.println("Device: ");
      display.setCursor(64,8);
      display.println(deviceName);
      display.setCursor(0,16);
      display.print("PGValue: ");
      display.setCursor(64,16);
      display.println(progressValue);
      display.setCursor(0,24);
      display.print("Status: ");
      display.setCursor(64,24);
      display.print(status);
      display.display();  
    }
  }
}
