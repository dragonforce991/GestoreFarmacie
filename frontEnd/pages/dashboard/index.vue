<template>
  <div>
    {{ $store.state.user || 'CAzzi' }}
    <v-btn @click="$store.commit('setTitle', 'ASD')"></v-btn>
  </div>
</template>

<script>
export default 
{
  layout: 'dashboard'
}
</script>