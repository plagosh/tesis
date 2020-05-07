# -*- coding: utf-8 -*-
"""
Created on Mon May  4 00:08:07 2020

@author: Ruben
"""
import datetime

import os.path as path
import requests
import mysql.connector

mydb = mysql.connector.connect(
  host="localhost",
  user="paulina",
  passwd="4bd65f797b*",
  database="accionin_yotrabajoconpecs",
  charset='utf8'
  
)

# Datos FTP
ftp_servidor = 'yotrabajoconpecs.ddns.net'
ftp_usuario  = 'paulina'
ftp_clave    = '4bd65f797b*'
ftp_raiz     = '/public_html' 

indiceTempo=''
try:
    busca3="SELECT * FROM acutualizarepo"
    #print(busca)
    mycursor3 = mydb.cursor()
    mycursor3.execute(busca3)
    myresult3 = mycursor3.fetchall()
    indiceTempo=len(myresult3)+1
except:
    pass
    
stt_indice=str(indiceTempo)

try:
    busca2="SELECT * FROM repo2"
    #print(busca)
    mycursor2 = mydb.cursor()
    mycursor2.execute(busca2)
    myresult2 = mycursor2.fetchall()
    indice=len(myresult2)+1
except:
    pass
    
deuda=0

diccionario_url='https://api.arasaac.org/api/keywords/es'
dicc=requests.get(diccionario_url)
final_dicc=dicc.json()
dicc=final_dicc['words']   
i=0
while(i!=(len(dicc)-1)):
#while(i!=26):
    #print(dicc[i])
    #print(i)
    palabrota=dicc[i]
    print(str(i+1)+' -> '+palabrota)
    
    try:
        busca="SELECT * FROM repo2 WHERE Nombre like '"+palabrota+"'"
        #print(busca)
        mycursor = mydb.cursor()
        mycursor.execute(busca)
        myresult = mycursor.fetchall()
        #print(myresult)
        #print(len(myresult))
    except:
        pass
        
    if (myresult):
        for x in myresult:
            print(x)
            pass
    else:
        #print('te debo la palabra: '+palabrota)
        
        #print(palabrota[i]+' -> Estoy en la linea '+str(i)+' de '+str(len(final_dicc['words'])))
    
        url = 'https://api.arasaac.org/api/pictograms/es/search/'+str(palabrota)
        args={'nombre':'args'}
    
        response=requests.get(url,params=args)
             
        if response.status_code==200:
            response_json=response.json()
            origin=response_json[0]
            imga=str(origin['_id'])
            print(origin['_id'])
            url_imgae='https://api.arasaac.org/api/pictograms/'+str(imga)+'?plural=false&color=true&backgroundColor=%23FFFFFF&resolution=500&skin=white&hair=darkBrown&url=true&download=true'
            
            try:
                re=requests.get(url_imgae)
            except:
                re=''
                print('super error')
                print(re)
            
            try:
                final_url=re.json()
            except():
                pass
            
            try:
                 print(final_url['image'])
            except(ValueError):
                print('me salte imagen')
                pass
            keylead=origin['keywords']
            leer=keylead[0]
            
            try: 
                tipo=str(leer['type'])
            except(KeyError): 
                tipo=str('none')
            
            print(origin['_id'])
           
            add_id=str(origin['_id'])
            add_nombre=str(palabrota)
            add_type=str(tipo)
            
            
            if (final_url):                                                       
                id_imagen = str(origin['_id'])+'.png'
                url_imagen = final_url['image'] # El link de la imagen
            
                imagen = requests.get(url_imagen).content #buscamos la imagen
                if path.exists('c:/xampp/htdocs/yotrabajoconpecs/repo/img/' + id_imagen):
                    print(str(id_imagen)+'-> imagen Existe')
                    pass
                else:
                    with open('c:/xampp/htdocs/yotrabajoconpecs/repo/img/' + id_imagen, 'wb') as handler:
                        handler.write(imagen)
                    print(str(id_imagen)+'-> imagen guardada')
                
                add_imagen=str('repo/img/' + id_imagen)
                
                mycursor = mydb.cursor()
                
                st_indice=str(indice)
                
                val = st_indice, add_id, add_nombre, add_type, add_imagen
                sql = "INSERT INTO `repo2` (`idnu`, `ID`, `Nombre`, `Tipo`, `Imagen`) VALUES "+str(val)+";"
                print(sql)
                mycursor.execute(sql)
                
                mydb.commit()
    
                print(mycursor.rowcount, "record inserted.")
                add_id=""
                add_nombre=""
                add_type=""
                add_imagen=""
                final_url=""
                indice=indice+1;
            
                deuda=deuda+1
    i=i+1

print("le debo al diccionario: "+str(deuda)+" palabras")


tempo=str(datetime.datetime.now())
estado='Finalizado'

val = stt_indice, tempo, estado, deuda
sql = "INSERT INTO `acutualizarepo` (`Id`, `FechaHR`, `Estado`, `CanADD`) VALUES "+str(val)+";"
print(sql)
mycursor3.execute(sql)

mydb.commit()




