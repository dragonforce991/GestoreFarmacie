<template>
  <v-form
    ref="form"
    @submit.prevent="send"
  >
    <v-text-field
      v-model="text"
      label="Message..."
      outlined
      :rules="rules"
      append-icon="mdi-send-circle-outline"
      @input="typing"
      @click:append="send"
      @blur="resetValidation"
    />
  </v-form>
</template>

<script>

export default {
  data: () => ({
    text: "",
    rules: [v => !!v || "Text is required"],
  }),
  computed: {
    //...mapGetters(["typingStatus"]),
  },
  methods: {
    //...mapActions(["createMessage", "setTypingStatus"]),
    send() {
      /*if (this.$refs.form.validate()) {
        this.createMessage(this.text); //push message on firebase
        this.text = ""; 

        this.setTypingStatus(false); //insieme a questo
        this.resetValidation();
      }*/
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    },
    typing() {
      /*if (!this.typingStatus) {
        this.setTypingStatus(true); //update su firebase
      }*/
    },
  },
};
</script>
