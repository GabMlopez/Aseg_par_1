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
        `${BASE_URL}/auth/login`,
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

    // Ventas
    let ventas=http.get(
        `${BASE_URL}/ventas`,
        authHeaders
    );

    check(ventas,{
        'ventas responde':(r)=>r.status===200
    });

    sleep(1);

}