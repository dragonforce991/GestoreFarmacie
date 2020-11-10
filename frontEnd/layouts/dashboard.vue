<template>
  <v-app style="background: white">
    <v-navigation-drawer app enable-resize-watcher v-model="drawer" width="280">
      <v-list expand>
        <drawer-element v-for="page in tree" :key="page.id" :item="page" />
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

    <v-slide-y-transition>
      <OperationalAppBar key="appBar" v-if="$store.state.appBar.visible"></OperationalAppBar>
    </v-slide-y-transition>

    <snackbar />

    <simple-confirmation-dialog
      @result="result" ref="confirmationDialog"
      title="Conferma operazione" description="Sei sicuro di voler uscire ?"
      negativeButtonText="Annulla" positiveButtonText="Conferma" />
  </v-app>
</template>

<script>
import OperationalAppBar from '~/components/dashboard/operational_app_bar';
import firebase from "~/plugins/firebase.js";
export default
{
  middleware: 'auth',

  components: {
    OperationalAppBar,
  },

  data()
	{
		return {
      drawer: true,
      isMounted : false
		}
  },
  mounted(){
      const db = this.$fire.firestore;
      const username = this.$store.state.user.id;
      db
      .collection("chat")
      .where("users", "array-contains", username)
      .onSnapshot((querySnapshot) =>{
        if(this.isMounted){
          const changed = querySnapshot.docChanges()[0].doc.data()
          if(changed.lastModifiedBy !== username){
            if(querySnapshot.docChanges()[0].type == "added" && changed.isGroup){
              let message = "Sei stato aggiunto al gruppo " + changed.Name;
              this.$notifier.showInfo(message);
            }
            if(querySnapshot.docChanges()[0].type == "added" && !changed.isGroup){
              const name = changed.Name.split("-")[0] == this.$store.state.user.name + " " + this.$store.state.user.cognome ? changed.Name.split("-")[1] : changed.Name.split("-")[0]
              let message = "Nuova Chat : " + name;
              this.$notifier.showInfo(message);
            }
            if(querySnapshot.docChanges()[0].type == "modified"){
              if(changed.isGroup){
                let message = "Nuovo messaggio in " + changed.Name;
                this.$notifier.showInfo(message);
              }else {
                const name = changed.Name.split("-")[0] == this.$store.state.user.name + " " + this.$store.state.user.cognome ? changed.Name.split("-")[1] : changed.Name.split("-")[0]
                let message = "Nuovo messaggio da " + name
                this.$notifier.showInfo(message);
              }
            }  
          }  
        }
        this.isMounted = true;
      })
    
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
          await this.$axios.$post('/logout');

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
li.nuxt-link-exact-active {
  background-color: #F6F6F6;
}
.v-dialog > .v-card > .v-toolbar {
  position: sticky;
  top: 0;
  z-index: 999;
}
</style>
