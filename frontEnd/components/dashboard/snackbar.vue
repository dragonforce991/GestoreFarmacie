<template>
	<v-expand-transition>
		<v-snackbar	v-model="show" :color="color" bottom right>
			{{ message }}

			<template v-slot:action>
				<v-btn text @click="show = false">Chiudi</v-btn>
			</template>
		</v-snackbar>
	</v-expand-transition>
</template>

<script>
	export default
	{
		data ()
		{
			return {
				show: false,
				message: '',
				color: ''
			}
		},

		mounted ()
		{
			this.focusSearchUnsubscriber = this.$store.subscribe((mutation, state) =>
			{
				if (mutation.type === 'notifier/showMessage')
				{
					this.message = state.notifier.notification.content
					this.color = state.notifier.notification.color
					this.show = true
				}
			})
		}
	}
</script>
