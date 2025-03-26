const tools = {
    copy: function (target, source) {
        Object.keys(target).forEach(key => {
            if (key === 'cover') {
                source['imageFiles'] = target[key];
            } else if (key === 'lyric') {
                source['textFiles'] = target[key];
            }
            if (key in source) {
                source[key] = target[key];
            }
        });
        return source;
    }
}
export default tools;