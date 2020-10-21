<template>
	<v-dialog	v-model="dialog" max-width="300" persistent>
		<v-card>
			<v-card-title class="headline">{{title}}</v-card-title>

			<v-card-text>
				{{description}}
			</v-card-text>

			<v-card-actions>
				<v-spacer></v-spacer>

				<v-btn color="primary" :disabled="loading" text @click="choose(false)">
					{{negativeButtonText}}
				</v-btn>

				<v-btn color="primary" :loading="loading" text @click="choose(true)">
					{{positiveButtonText}}
				</v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script>
export default 
{
	name: 'simple-confirmation-dialog',
	
	props: ['title', 'description', 'negativeButtonText', 'positiveButtonText', 'loading'],

	data() 
	{
		return {
			dialog: false
		}
	},

	methods: 
	{
		show() {
			this.dialog = true;
		},

		hide() {
			this.dialog = false;
		},

		choose(value) {
			this.$emit('result', value);
		}
	}
}
</script>
