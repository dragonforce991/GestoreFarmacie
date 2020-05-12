<template>
  <v-app id="inspire">
    <v-navigation-drawer v-model="drawer" app>
      <v-list dense>
        <div
            v-for="(tab,i) in tabs"
            v-bind:key="i"
            color="primary"
            @click="currentTab=tab"
            
          >
          <v-list-item link>
            <v-list-item-action>
              <v-icon>{{tab.icon}}</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>{{tab.name}}</v-list-item-title>
            </v-list-item-content>
            </v-list-item>
          </div>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app color="indigo" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Gestore Farmacie</v-toolbar-title>
    </v-app-bar>

    <v-content>
      <v-container class="fill-height" fluid>
        <v-row align="stretch" justify="center">
        <keep-alive>
            <component v-bind:is="currentTab.component"></component>
        </keep-alive>
        </v-row>
      </v-container>
    </v-content>
    <v-footer color="indigo" app>
      <span class="white--text">&copy; 2019</span>
    </v-footer>
  </v-app>
</template>

<script>
import tabVendita from "./tabVendita";
import tabInserisciutente from "./tabInserisciUtente";
export default {
  name: "homePage",
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
                icon:"shopping_basket",
                component: tabVendita
            },
            {
                name:"InserisciUtente",
                icon:"group_add",
                component: tabInserisciutente
            }
        ],
      drawer: false
    };
  },
  created() {
      //this.tabs.push(object {name,icon,component}) per gestire la visibilità sui profili
      this.currentTab = this.tabs[0];
  },
};
</script>