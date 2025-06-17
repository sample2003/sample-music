/*
async startStream(content) {
  try {
    this.streamingText = '' // 清空上次内容Ni
    this.controller = new AbortController() // 创建中断控制器

    const response = await fetch('http://localhost:11434/api/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        model: "deepseek-r1:8b",
        messages: [
          { role: "user", content: content }
        ],
        stream: true
      }),
      signal: this.controller.signal // 绑定中断信号
    })
    const reader = response.body.getReader()
    const decoder = new TextDecoder()

    // 实时流式处理
    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      const chunk = decoder.decode(value)
      const data = JSON.parse(chunk)

      console.log(data)

      // 逐字追加到 streamingText（模拟逐字效果）
      if (data.message?.content) {
        this.streamingText += data.message.content
        await this.$nextTick() // 确保 Vue 渲染更新
      }
    }
  } catch (error) {
    if (error.name !== 'AbortError') {
      console.error('请求失败:', error)
    }
  }
},*/

