# -*- coding: utf-8 -*-
"""
Created on Thu Apr 30 19:45:29 2020

@author: Paulina
"""
import requests
import datetime

from os import remove


def limpiar_txt():
    remove("listado.txt")
    return

def marcaTiempoInicio():
    global ini
    ini='inicio: '+str(datetime.datetime.now())
    return

def marcaTiempoFinal():
    fu=''
    fu=open('listado.txt','a')
    fu.write('\n'+ini+' final: '+str(datetime.datetime.now()))
    fu.close()
    return

print("Bienvenidos")

diccionario_url='https://api.arasaac.org/api/keywords/es'

dicc=requests.get(diccionario_url)
final_dicc=dicc.json()
j=0 #palabra inicial

#limpiar_txt()

marcaTiempoInicio()

while (j!=len(final_dicc['words'])):
#while (j!=1345):
    palabrotas=final_dicc['words']
    print(palabrotas[j]+' -> Estoy en la linea '+str(j)+' de '+str(len(final_dicc['words'])))
    
    url = 'https://api.arasaac.org/api/pictograms/es/search/'+str(palabrotas[j])
    args={'nombre':'ruben'}
    
    response=requests.get(url,params=args)
    #print(response)
    
    if response.status_code==200:
        response_json=response.json()
        i=0
        #print(len(response_json))
        while (i!=1):
        #while (i!=len(response_json)):
            
            origin=response_json[i]
            i=i+1
            keylead=origin['keywords']
            leer=keylead[0]
            
            try: 
                tipo=str(leer['type'])
            except(KeyError): 
                tipo=str('none')

            try: 
                tipo=str(leer['type'])
            except(KeyError): 
                tipo=str('none')

                
            print(str(origin['_id'])+" -> "+leer['keyword']+" -> "+tipo)
            print(leer['keyword'])
            
            imga=str(origin['_id'])
            url_imgae='https://api.arasaac.org/api/pictograms/'+str(imga)+'?plural=false&color=true&backgroundColor=%23FFFFFF&resolution=500&skin=white&hair=darkBrown&url=true&download=true'
            try:
                re=requests.get(url_imgae)
            except:
                re=''
                print('super error')
            print(re)
            try:
                final_url=re.json()
            except(ValueError):
                pass
            try:
                 print(final_url['image'])
            except(ValueError):
                print('me salte imagen')
                
                pass

           
            
            if (final_url):                                                       
                id_imagen = str(origin['_id'])+".png"
                url_imagen = final_url['image'] # El link de la imagen
            
             
                imagen = requests.get(url_imagen).content #buscamos la imagen
            
                with open('img\ ' + id_imagen, 'wb') as handler:
                    handler.write(imagen)
                print(str(id_imagen)+'-> imagen guardada')
            
            f=open('listado.txt', 'a')
            f.write('\n' + str(origin['_id'])+";"+leer['keyword']+";"+tipo)
            f.close()
    
    j=j+1
    
    
marcaTiempoFinal()
