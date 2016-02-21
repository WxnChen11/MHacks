double channel0; //throttle receiver
double channel1; //rudder receiver
double x0;
double x1;
int elevator = 3;
int aileron = 10;
int throttle = 11;
int rudder = 12;
double wiie;
double wiir;
double wiia;
double wiit;
int switchd;
int commaIndex;
int secondcommaIndex;
int thirdcommaIndex;
int fourthcommaIndex;
String serialIn;
String firstValue;
String secondValue;
String thirdValue;
String fourthValue;
double first;
double second;
double third;
double fourth;

void func (double wiirightelev,
double wiileftthrot,
double wiileftrudd,
double wiirightail) {

/*
for (int y= 0; y<2; y++){ 
   channel0[y] = pulseIn(5, HIGH); //throttle
} 
for (int i = 0; i <2; i++){
   channel1[i] = pulseIn(6, HIGH); //elevator
}
*/
channel0 = pulseIn(5, HIGH);//throttle
channel1 = pulseIn(6, HIGH); //rudder


x0 = ((channel0-1290)*255/1300)-1;
x1 = ((channel1-1940)*64/325)-1;

 
//read switch D13002200,2200,2200,2200

if (2000 < 1900)

{
  analogWrite (rudder, x1);
  analogWrite (throttle, x0);
  if (channel1>=2590){
    analogWrite (rudder, 2589);}
  if (channel1<=1310){
    analogWrite (rudder, 1310);}
    
 Serial.print(channel0);
  Serial.print("  |  ");
  Serial.print(x0);
  Serial.print("  |  ");
  Serial.print(channel1); 
  Serial.print("  |  ");
  Serial.println(x1);
}
else
{
 
  wiie = ((wiirightelev-1940)*64/325)-1; 
  wiit = ((wiileftthrot-1290)*255/1300)-1; 
  wiir = ((wiileftrudd-1290)*255/1300)-1; 
  wiia = ((wiirightail-1940)*64/325)-1;
  //analogWrite (elevator,wiie );
  analogWrite (throttle,wiit );  
  //analogWrite (rudder, wiir); 
  //analogWrite (aileron, wiia);

  Serial.println(wiit);

/*
  Serial.println(wiirightelev);  
  Serial.print("  |  ");  
  Serial.println(wiie);
    Serial.print("  |  ");
   Serial.println(wiirightelev);
    Serial.print("  |  ");
  Serial.println(wiit);
      Serial.print("  |  ");
   Serial.println(wiileftthrot);
    Serial.print("  |  ");
  Serial.println(wiir);
    Serial.print("  |  ");
       Serial.println(wiileftrudd);
    Serial.print("  |  ");
  Serial.println(wiia);*/

  
 /* 
  //////////elev//////////
  if ( wiirightelev < 1300){
    analogWrite (elevator, 1300);}
  else 
    { if (wiirightelev > 2580)
      {analogWrite (elevator, 2575);}
  else {analogWrite (elevator,wiie );}
  }
  /////////throt///////////////////
  if ( wiileftthrot < 1300){
    analogWrite (throttle, 1300);}
  else 
    { if (wiileftthrot > 2580)
      {analogWrite (throttle, 2575);}
  else {analogWrite (throttle,wiit );}
  }
  //////rudd/////////////////
    if ( wiileftrudd < 1300){
    analogWrite (rudder, 1300);}
  else 
    { if (wiileftrudd > 2580)
      {analogWrite (rudder, 2575);}
  else {analogWrite (rudder,wiir );}
  }
  //////ail/////////////
    if ( wiirightail < 1300){
    analogWrite (aileron, 1300);}
  else 
    { if (wiirightail > 2580)
      {analogWrite (aileron, 2575);}
  else {analogWrite (aileron,wiia );}
  }
  ////////////////////////
  /*
  */
  
}
} 
void setup() {
  // put your setup code here, to run once:

  pinMode (5, INPUT); //from receiver throttle
  pinMode (6, INPUT); //from receiver elevator
  pinMode (elevator, OUTPUT); //to HK elevator
  pinMode (aileron, OUTPUT); //to HK aileron
  pinMode (throttle, OUTPUT); //to HK throttle
  pinMode (rudder, OUTPUT); //to HK rudder
  Serial.begin (9600);
}

void loop () {
   //func (2590.00, 2590.00 , 2590.00, 2590.00);

   if (Serial.available()) {
      //serialIn = Serial.readString();
      serialIn = Serial.readStringUntil('+');
      //Serial.println(serialIn);
     commaIndex = serialIn.indexOf(',');
    //  Search for the next comma just after the first
    secondcommaIndex = serialIn.indexOf(',', commaIndex+1);
    thirdcommaIndex = serialIn.indexOf(',', secondcommaIndex+1);
    //fourthcommaIndex = serialIn.indexOf(',', thirdcommaIndex+1);
     firstValue = serialIn.substring(0, commaIndex);
     secondValue = serialIn.substring(commaIndex+1, secondcommaIndex);
     thirdValue = serialIn.substring(secondcommaIndex+1, thirdcommaIndex);
     fourthValue = serialIn.substring(thirdcommaIndex, -1);

     
  func (firstValue.toFloat(), secondValue.toFloat(), thirdValue.toFloat(), fourthValue.toFloat());
  
}}




