import axios from "axios";
import router from "@/router";

const service = axios.create({
    baseURL: "http://localhost:8080",
})

service.interceptors.request.use(config => {
    const token = localStorage.getItem('userToken');
    if (token) {
        config.headers.Authorization = token;
    }
    return config;
}, error => {
    throw error; // 直接抛出错误
});

service.interceptors.response.use(
    (response) => {
        // 任何 HTTP 状态码为 2xx 的响应数据都会触发此函数
        // 对响应数据做点什么
        if (response.data.code === 200) {
            return response.data; // 返回数据部分
        } else {
            return response.data.code;
        }
    },
    (error) => {
        if (
            error.message.includes("Network Error") ||
            error.message === "ECONNABORTED"
        ) {
            // 后端服务器关闭时，跳转到错误页面
            router.push({ name: "ServerClose" }).catch((err) => console.error(err));
        } else {
            // 处理其他错误
        }
        throw error; // 重新抛出错误，以便可以在组件中进一步处理
    }
);
export default service;