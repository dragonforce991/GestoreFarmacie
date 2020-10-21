<template>
  <v-container fluid>
    <v-row v-if="authError" justify="center">
      <v-col cols="12" align="center">
        <img src="/searching.svg" width="400" height="300">
      </v-col>

      <v-col cols="12" align="center">
        <h1 class="display-2 green--text">Whoops...</h1>
      </v-col>

      <v-col cols="12" align="center">
        <p class="mt-4 headline">
          Hey {{$store.state.user.name}}, sei ancora qui ?
        </p>
      </v-col>

      <v-col cols="4" align="center">
        <login-card title="" buttonText="Verifica" :loginDetails="{email: $store.state.user.email}" @loggedIn="loginCallback($event)"></login-card>
      </v-col>
    </v-row>

    <v-row v-if="serverError">
      <v-col cols="12" align="center">
        <img src="/server_down.svg" width="400" height="300">
      </v-col>

      <v-col cols="12" align="center">
        <h1 class="display-2 green--text">Whoops...</h1>
      </v-col>

      <v-col cols="12" align="center">
        <p class="mt-4 headline">
          Pare che il server abbia qualche problema, riprova più tardi
          <br>o<br>
          Torna alla <nuxt-link :to="homeLink">Home</nuxt-link>
        </p>
      </v-col>
    </v-row>

    <v-row v-if="pageNotFound">
      <v-col cols="12" align="center">
        <img src="/page_not_found.svg" width="400" height="300">
      </v-col>

      <v-col cols="12" align="center">
        <h1 class="display-2 green--text">Whoops...</h1>
      </v-col>

      <v-col cols="12" align="center">
        <p class="mt-4 headline">
          Pagina non trovata torna alla <nuxt-link :to="homeLink">Home</nuxt-link>
        </p>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default
{
  layout: 'empty',

  props:
  {
    error: { type: Object, default: null }
  },

  mounted()
  {
    if(this.authError)
      this.$store.commit('setLoggedIn', false);
  },

  methods:
  {
    async loginCallback(response)
    {
      this.$store.commit('setUser', response.user);
      this.$store.commit('setLoggedIn', true);
      
      if(this.error.config.method === 'post' || this.error.config.method === 'put' || this.error.config.method === 'delete')
        await this.$axios(this.error.config);

      window.location.reload(true)
    }
  },

  computed:
  {
    homeLink() {
      return this.$store.state.user ? this.$store.state.user.role.default_path : '/'
    },

    pageNotFound() {
      return this.error.statusCode === 404
    },

    authError() {
      return this.error.statusCode === 401 || this.error.statusCode === 403
    },

    serverError() {
      return this.error.statusCode === 500 || this.error.statusCode === 400
    }
  }
}
</script>