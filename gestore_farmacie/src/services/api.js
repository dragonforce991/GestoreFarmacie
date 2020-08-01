import axios from 'axios'

export default()=>
{
    return axios.create({
        //baseURL: 'http://192.168.1.4:8080'
        baseURL: 'http://192.168.1.3:8080/Login'  //modifica per integrare con servlet
    })
}