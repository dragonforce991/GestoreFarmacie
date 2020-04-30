var App = Vue.component('App', {
    template: `
 <div>
    <div class="container">
        <div v-if="isAbilited">
            <button class="btn btn-primary">bottone di esempio</button>
           
        </div>
        <div v-else>
            {{messageElse}}
        </div>
    </div>
</div>
    `,
    data() {
        return {
            isAbilited: true,
            messageElse: "Utente non abilitato"
        };

    }
});

new Vue({
    el: "#app"
});