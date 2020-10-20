<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="4" md="6">
        <login-card @loggedIn="loginCallback($event)"></login-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default
{
  middleware: ['notAuth'],

  methods:
  {
    loginCallback(response)
    {
      this.$store.commit('setUser', response.user);
      this.$store.commit('setLoggedIn', true);

      this.$router.replace({ path: response.user.role.default_path });
    }
  }
}
</script>