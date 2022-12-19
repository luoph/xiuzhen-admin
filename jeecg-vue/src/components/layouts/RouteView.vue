<template>
  <div class="main">
    <keep-alive>
      <router-view/>
    </keep-alive>
    <!-- <router-view v-if="!keepAlive" /> -->
  </div>
</template>

<script>
export default {
  name: "RouteView",
  data() {
    return {
      isRouteView: true
    };
  },
  computed: {
    keepAlive() {
      return this.$route.meta.keepAlive;
    }
  }
}
</script>
