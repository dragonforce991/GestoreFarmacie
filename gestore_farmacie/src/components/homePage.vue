<template>
  <v-app id="inspire">
    <v-navigation-drawer v-model="drawer" app>
      <v-list shaped>
        <div
            v-for="(tab,i) in tabs"
            v-bind:key="i"
            color="primary"
            @click="currentTab=tab"
            
          >
          <template v-if = "tab.condition">
            <v-list-item  link>
              <v-list-item-action>
                <v-icon>{{tab.icon}}</v-icon>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>{{tab.visualName}}</v-list-item-title>
              </v-list-item-content>
              </v-list-item>
          </template>
          </div>
      </v-list>

    </v-navigation-drawer>

    <v-app-bar app color="indigo" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Gestore Farmacie</v-toolbar-title>
    </v-app-bar>

    <v-content>
       <!-- class="fill-height" -->
      <v-container fluid>
        <!-- <v-row align="stretch" justify="center"> -->
        <keep-alive>
            <component v-bind:is="currentTab.component"></component>
        </keep-alive>
        <!-- </v-row> -->
      </v-container>
    </v-content>
    <v-footer color="indigo" app>
      <v-spacer></v-spacer>
      <div class="white--text"><v-icon>tab</v-icon> {{ currentTab.visualName }}</div>
      <!-- <span class="white--text">&copy;{{currentTab.name}}</span> -->
    </v-footer>
  </v-app>
</template>

<script>
import tabVendita from "./tabVendita";
import tabInserisciutente from "./tabInserisciUtente";
import tabGestioneAccount from "./tabGestioneAccount";
export default {
  name: "homePage",
  props:{
        user: Object
      },
//   components: {
//     tabVendita,
//     tabInserisciutente
//   },
  data() {
    return {
      
      currentTab: {},
      tabs: [
            {
                name:"Vendita",
                visualName: "Vendita",
                icon:"shopping_basket",
                component: tabVendita,
                condition: this.user.profileName == "REG" || this.user.profileName == "TF"
            },
            {
                name:"InserisciUtente",
                visualName: "Gestione Utenti",
                icon:"group_add",
                component: tabInserisciutente,
                condition: this.user.profileName == "REG"
            },
            {
                name: "GestioneAccount",
                visualName: "Gestione Account",
                icon:"person",
                component: tabGestioneAccount,
                condition: true
            }
        ],
      drawer: false
    };
  },
  created() {
      //this.tabs.push(object {name,icon,component}) per gestire la visibilità sui profili
      this.currentTab = this.tabs[0];
      console.log(this.user);
  },
};
</script>