export const state = () => ({
	notification: { content: '', color: '' },
});

export const mutations =
{
	showInfo (state, payload) {
		state.notification.content = payload.content
		state.notification.color = payload.color
	},
};
