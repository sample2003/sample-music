const ChatService = {
  streamGenerate: async function* (prompt, signal) {
    try {
      const response = await fetch('/api/chat/generate/stream', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('userToken')
        },
        body: JSON.stringify({
          prompt: prompt
        }),
        signal // 绑定中断信号
      });
      const reader = response.body.getReader();
      const decoder = new TextDecoder();

      while (true) {
        const { done, value } = await reader.read();
        if (done) break;

        // 直接返回解码后的文本块（无需事件解析）
        yield decoder.decode(value, { stream: true });
      }
    } catch (error) {
      if (error.name !== 'AbortError') {
        console.error('请求失败:', error);
        throw error;
      }
    }
  }
};

export default ChatService;