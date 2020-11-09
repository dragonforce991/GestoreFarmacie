<template>
  <div>
    <v-list-group v-if="item.children && item.children.length > 0" color="primary" :prepend-icon="item.icon">
        <template v-slot:activator>
          <v-list-item-content>
            <v-list-item-title style="font-weight: 500;">{{item.title}}</v-list-item-title>
          </v-list-item-content>
        </template>

        <v-icon slot="prependIcon">{{ item.icon }}</v-icon>

      <drawer-element class="ml-4" v-for="(child, index) in item.children" :item="child" :key="index" />
      </v-list-group>

    <nuxt-link v-else :to="item.url" tag="li">
      <v-list-item>
        <v-list-item-action>
          <v-icon>{{ item.icon }}</v-icon>
        </v-list-item-action>

        <v-list-item-content>
          <v-list-item-title class="text-xs-left">{{ item.title }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </nuxt-link>
  </div>
</template>

<script>
export default
{
  name: 'drawer-element',
	props: ['item']
}
</script>
