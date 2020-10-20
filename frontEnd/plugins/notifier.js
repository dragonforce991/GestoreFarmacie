export default ({ app, store }, inject) =>
{
	inject('notifier',
	{
		showInfo(message) {
			store.commit('notifier/showMessage', { content: message, color: 'info' })
		},

		showError(message) {
			store.commit('notifier/showMessage', { content: message, color: 'error' })
		},

		showWarning(message) {
			store.commit('notifier/showMessage', { content: message, color: 'warning' })
		}
	})
}
