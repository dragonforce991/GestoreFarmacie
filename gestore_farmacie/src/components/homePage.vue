<template>
    <div>
        <v-btn 
            v-for ="tab in tabs" 
            v-bind:key="tab"
            color ="primary"
            @click= "currentTab=tab"
            :value = "tab"
        >{{tab}}</v-btn>
        <keep-alive>
            <component v-bind:is="currentTabComponent"></component>
        </keep-alive>
    </div>
</template>

<script>
import tabVendita from './tabVendita';
import tabInserisciutente from './tabInserisciUtente'
export default {
    name: 'homePage',
    components: {
        tabVendita,tabInserisciutente
    },
    data(){
        return {
            currentTab: 'Vendita',
            tabs: ['Vendita', 'InserisciUtente']
        }
    },
    computed: {
        currentTabComponent: function(){
            return 'tab-' + this.currentTab.toLowerCase() 
        }
    }
}
</script>