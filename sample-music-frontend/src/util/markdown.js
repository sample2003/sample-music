import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'

const md = new MarkdownIt({
    html: true,         // 允许 HTML 标签
    linkify: true,      // 自动转换链接
    typographer: true,  // 优化排版
    highlight: function (str, lang) { // 代码高亮
        if (lang && hljs.getLanguage(lang)) {
            try {
                return `<pre class="hljs"><code>${hljs.highlight(lang, str, true).value}</code></pre>`
            } catch (__) {}
        }
        return ''
    }
})

export default md