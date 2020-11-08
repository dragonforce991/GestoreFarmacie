<template>
  <v-card elevation="4">
    <v-container @keyup.enter = "login">
      <v-alert type="error" v-if="errorMessage !== ''">
        {{ errorMessage }}
      </v-alert>

      <v-card-title>{{ title }}</v-card-title>

      <v-card-text>
        <v-form v-model="validForm">
          <v-text-field
            v-model="email"
            :rules="rules"
            outlined
            dense
            label="Email"
          ></v-text-field>
          <v-text-field
            v-model="password"
            outlined
            dense
            autocomplete="password"
            label="Password"
            :rules="rules"
            :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append="show = !show"
            :type="show ? 'text' : 'password'"
          />
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-btn
          @click="login"
          :disabled="!validForm"
          :loading="loading"
          block
          color="primary"
          >{{ buttonText }}</v-btn
        >
      </v-card-actions>
    </v-container>
  </v-card>
</template>

<script>
export default {
  name: "login-card",

  props: {
    title: {
      type: String,
      default: "Login",
    },

    loginDetails: {
      type: Object,
      default: () => {},
    },

    buttonText: {
      type: String,
      default: "Entra",
    },
  },

  data() {
    return {
      email: this.loginDetails ? this.loginDetails.email : "",
      password: this.loginDetails ? this.loginDetails.password : "",
      loading: false,
      errorMessage: "",
      show: false,
      validForm: false,
      rules: [(v) => !!v || "Campo necessario"],
    };
  },

  methods: {
    async login() {
      this.loading = true;
      this.errorMessage = "";

      try {
        const response = await this.$axios.$post('/Login', { email: this.email, password: this.password });
        this.$emit("loggedIn", response);
      } catch (e) {
        if (e.response.status !== 500) {
          this.$emit("errorLoggingIn", e);
          this.errorMessage = e.response.data.message;
        } else this.$nuxt.error(e);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>