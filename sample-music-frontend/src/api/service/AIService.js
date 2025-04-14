import service from "@/api/request";
import axios from "axios";

const ChatService = {
    getChat: function(content) {
        return axios.post('http://localhost:11434/api/chat', {
            model: "deepseek-r1:8b",
            messages: [
                { role: "user", content: content }
            ],
            stream: false // 明确关闭流式模式
        }, {
            headers: { 'Content-Type': 'application/json' }
        })
            .then(response => {
                // 提取有效内容（根据 Ollama API 文档结构）
                return response.data.message?.content || 'No response';
            })
            .catch(error => {
                console.error('API Error:', error.response?.data || error.message);
                throw error;
            });
    },
    streamChat: async function(content) {
        try {
            const response = await fetch('http://localhost:11434/api/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    model: "deepseek-r1:8b",
                    messages: [
                        { role: "user", content: content }
                    ],
                    stream: true
                })
            });

            const reader = response.body.getReader();
            const decoder = new TextDecoder();

            while (true) {
                const { done, value } = await reader.read();
                if (done) break;

                const chunk = decoder.decode(value);
                const data = JSON.parse(chunk);
                console.log('流式片段:', data.message?.content || '');
            }
        } catch (error) {
            console.error('请求失败:', error);
        }
    }
};

export default ChatService;