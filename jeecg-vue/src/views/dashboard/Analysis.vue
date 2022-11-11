<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
    </a-row>

    <a-card :bordered="false" :body-style="{ padding: '0' }">
    </a-card>

  </div>
</template>

<script>
  import IndexChart from './IndexChart'
  import IndexTask from "./IndexTask"
  import IndexBdc from './IndexBdc'

  export default {
    name: "Analysis",
    components: {
      IndexChart,
      IndexTask,
      IndexBdc
    },
    data() {
      return {
        indexStyle:1
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>