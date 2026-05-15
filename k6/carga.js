import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 20 },
        { duration: '1m', target: 50 },
        { duration: '30s', target: 100 },
        { duration: '30s', target: 0 },
    ],

    thresholds: {
        http_req_duration: ['p(95)<1000'],
        http_req_failed: ['rate<0.05']
    }
};

const BASE_URL='http://localhost:8080';

export default function () {

    let loginPayload = JSON.stringify({
        username:'admin',
        password:'admin123'
    });

    let params={
        headers:{
            'Content-Type':'application/json'
        }
    };

    let login=http.post(
        `${BASE_URL}/auth/login`,
        loginPayload,
        params
    );

    check(login,{
        'login correcto': (r)=>r.status===200
    });

    sleep(1);

    let inventario=http.get(
        `${BASE_URL}/inventario`
    );

    check(inventario,{
        'inventario responde': (r)=>r.status===200
    });

    sleep(1);

}