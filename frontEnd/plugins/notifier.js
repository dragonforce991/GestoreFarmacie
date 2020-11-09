export default ({ app, store }, inject) =>
{
	inject('notifier',
	{
		showInfo(message) {
			store.commit('notifier/showInfo', { content: message, color: 'info' })
		},

		showError(message) {
			store.commit('notifier/showInfo', { content: message, color: 'error' })
		},

		showWarning(message) {
			store.commit('notifier/showInfo', { content: message, color: 'warning' })
		}
	})
}
