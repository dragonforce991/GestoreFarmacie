<template>
  <v-app style="background: white">
    <v-navigation-drawer app enable-resize-watcher v-model="drawer" width="280">
      <v-list expand>
        <span v-for="page in tree" :key="page.id">
          <drawer-element :item="page"></drawer-element>
        </span>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar color="primary" app dark fixed flat>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>{{ title }}</v-toolbar-title>
      <v-spacer></v-spacer>

      <div>
        <v-avatar class="ml-3 mr-1">
          <v-tooltip bottom>
            <template v-slot:activator="{ on }">
              <v-icon v-on="on">{{role.icon}}</v-icon>
            </template>
            <span>{{role.name}}</span>
          </v-tooltip>
        </v-avatar>

        <v-menu offset-y transition="scale-transition">
          <template v-slot:activator="{ on }">
            <span id="name" v-on="on">{{ user.name }} {{ user.surname }}</span>
          </template>

          <v-list>
            <span class="overline ml-3 mb-2">Benvenuto!</span>

            <v-list-item>
              <v-list-item-action>
                <v-icon>mdi-account</v-icon>
              </v-list-item-action>

              <v-list-item-content>
                <v-list-item-title>Profilo</v-list-item-title>
              </v-list-item-content>
            </v-list-item>

            <v-divider></v-divider>

            <v-list-item @click="$refs.confirmationDialog.show()">
              <v-list-item-action>
                <v-icon>mdi-exit-to-app</v-icon>
              </v-list-item-action>

              <v-list-item-content>
                <v-list-item-title>Logout</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </v-app-bar>

    <v-main height="100%">
      <nuxt />
    </v-main>

    <snackbar></snackbar>

    <simple-confirmation-dialog @result="result" ref="confirmationDialog" title="Conferma operazione" description="Sei sicuro di voler uscire ?" negativeButtonText="Annulla" positiveButtonText="Conferma"></simple-confirmation-dialog>
  </v-app>
</template>

<script>
export default
{
  middleware: 'auth',

  data()
	{
		return {
      drawer: true,
		}
  },

  computed:
  {
    tree() {
      return this.$store.state.tree
    },

    user() {
      return this.$store.state.user || {}
    },

    role() {
      return this.user && this.user.role || {}
    },

    title() {
      return this.$store.state.title
    }
  },

  methods:
  {
    async result(val)
    {
      this.$refs.confirmationDialog.hide();

      if(val)
      {
        try
        {
          await this.$axios.$post('/auth/logout');

          this.$store.commit('auth/setUser', null);
          this.$store.commit('auth/setLoggedIn', false);

          this.$router.go({ path: '/' })
        }
        catch (e)
        {
          this.$nuxt.error(e);
        }
      }
    }
  }
}
</script>

<style>
#name {
  cursor: pointer;
}
img {
  max-width: 100%;
  max-height: 100%;
}
li {
  display: inline-block;
  width: 100%;
  font-weight: 500;
  text-decoration: none;
  cursor: pointer;
}
li:hover,
li.nuxt-link-active,
li.nuxt-link-exact-active {
  background-color: #F6F6F6;
}
.v-dialog > .v-card > .v-toolbar {
  position: sticky;
  top: 0;
  z-index: 999;
}
</style>