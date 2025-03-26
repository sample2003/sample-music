import ConfirmDialog from '@/dialog/confirmDialog.vue';
import ChangeDialog from '@/dialog/changeDialog.vue';
import MessageDialog from '@/dialog/messageDialog.vue';
import UpdateDialog from '@/dialog/updateDialog.vue';
import AddPlaylist from "@/dialog/AddPlaylist.vue";

const ConfirmDialogPlugin = {
    install(Vue) {
        Vue.prototype.$confirm = function (message, callback) {
            const ConfirmComponent = Vue.extend(ConfirmDialog);
            const instance = new ConfirmComponent({
                propsData: {
                    message: message,
                    isVisible: true
                }
            }).$mount();
            document.body.appendChild(instance.$el);

            const eventBus = new Vue();

            instance.$once('confirm', () => {
                callback(true);
                eventBus.$emit('close-dialog');
                instance.$destroy();
            });

            instance.$once('cancel', () => {
                callback(false);
                eventBus.$emit('close-dialog');
                instance.$destroy();
            });
            eventBus.$on('close-dialog', () => {
                document.body.removeChild(instance.$el); // 从DOM中移除对话框
            });
        };
        Vue.prototype.$change = function (message, items, callback) {
            const ChangeComponent = Vue.extend(ChangeDialog);
            const instance = new ChangeComponent({
                propsData: {
                    message: message,
                    isVisible: true,
                    items: items
                }
            }).$mount();
            document.body.appendChild(instance.$el);

            const eventBus = new Vue();

            instance.$once('confirm', (targetId) => {
                callback(true, targetId);
                eventBus.$emit('close-dialog');
                instance.$destroy();
            });

            instance.$once('cancel', () => {
                callback(false);
                eventBus.$emit('close-dialog');
                instance.$destroy();
            });
            eventBus.$on('close-dialog', () => {
                document.body.removeChild(instance.$el); // 从DOM中移除对话框
            });
        };
        Vue.prototype.$message = function (message) {
            const ChangeComponent = Vue.extend(MessageDialog);
            const instance = new ChangeComponent({
                propsData: {
                    message: message,
                    isVisible: true
                }
            }).$mount();
            document.body.appendChild(instance.$el);

            const eventBus = new Vue();
            setTimeout(() => {
                eventBus.$emit('close-dialog');
            }, 1500); // 0.5秒后执行

            eventBus.$on('close-dialog', () => {
                document.body.removeChild(instance.$el); // 从DOM中移除对话框
            });
        };
        Vue.prototype.$update = function (message, fields, callback) {
            const ChangeComponent = Vue.extend(UpdateDialog);
            const instance = new ChangeComponent({
                propsData: {
                    message: message,
                    fields: fields,
                    isVisible: true,
                }
            }).$mount();
            document.body.appendChild(instance.$el);

            const eventBus = new Vue();
            instance.$once('confirm', (fields) => {
                callback(true, fields);
                eventBus.$emit('close-dialog');
                instance.$destroy();
            });

            instance.$once('cancel', () => {
                callback(false);
                eventBus.$emit('close-dialog');
                instance.$destroy();
            });

            eventBus.$on('close-dialog', () => {
                document.body.removeChild(instance.$el); // 从DOM中移除对话框
            });
        };
        Vue.prototype.$addPlaylist = function () {
            const addPlaylistComponent = Vue.extend(AddPlaylist);
            const instance = new addPlaylistComponent({
                propsData: {
                    isVisible: true,
                }
            }).$mount();
            document.body.appendChild(instance.$el);
            const eventBus = new Vue();
            instance.$once('confirm', () => {
                eventBus.$emit('close-dialog');
                instance.$destroy();
            });

            instance.$once('cancel', () => {
                eventBus.$emit('close-dialog');
                instance.$destroy();
            });
            eventBus.$on('close-dialog', () => {
                document.body.removeChild(instance.$el); // 从DOM中移除对话框
            });
        }
    }
};

export default ConfirmDialogPlugin;