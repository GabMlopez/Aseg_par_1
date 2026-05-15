import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {

    stages: [
        { duration: '30s', target: 20 },
        { duration: '1m', target: 50 },
        { duration: '1m', target: 100 },
        { duration: '30s', target: 0 }
    ],

    thresholds: {
        http_req_duration: ['p(95)<1000'],
        http_req_failed: ['rate<0.05']
    }

};

const BASE_URL='http://localhost:8080';

export default function () {

    // Login
    let loginPayload = JSON.stringify({
        username:"admin",
        password:"admin123"
    });

    let params={
        headers:{
            'Content-Type':'application/json'
        }
    };

    let loginRes=http.post(
        `${BASE_URL}/auth`,
        loginPayload,
        params
    );

    check(loginRes,{
        'login correcto':(r)=>r.status===200
    });

    let token='';

    try{
        token=loginRes.json('token');
    }catch(e){}

    let authHeaders={
        headers:{
            Authorization:`Bearer ${token}`
        }
    };

    // Inventario
    let inventario=http.get(
        `${BASE_URL}/inventario`,
        authHeaders
    );

    check(inventario,{
        'inventario responde':(r)=>r.status===200
    });

    //Actualizar inventario para no quedarse sin prendas
    let inventarioActualizado=http.patch(
        `${BASE_URL}/inventario/1/stock?cantidad=20`,
        null,
        authHeaders
    );

    check(inventarioActualizado,{
        'inventario actualizado':(r)=>r.status===200
    });

    // Ventas - Vender 2 Camisetas
    let ventas=http.post(
        `${BASE_URL}/ventas/1?cantidad=2`,
        null,
        authHeaders
    );

    check(ventas,{
        'ventas responde':(r)=>r.status===200
    });

    sleep(1);

}