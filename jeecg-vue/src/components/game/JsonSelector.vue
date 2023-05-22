<template>
  <div>
    <a-select :mode="multiple ? 'multiple' : '-'" :placeholder='tips' @change='onOptionSelected'
              :filterOption='filterOption' showSearch
              allowClear style='width: 100%'>
      <a-select-option v-for='it in selectValues' :key='it.value' :value='it.value'>
        {{ it.name + ' [' + it.value + ']' }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script>
import { getAction } from '@/api/manage';

export default {
  name: 'JsonSelector',
  components: {
    getAction
  },
  props: {
    file: {
      type: String,
      required: true,
      default: ''
    },
    name: {
      type: String,
      required: true,
      default: ''
    },
    value: {
      type: String,
      required: true,
      default: ''
    },
    multiple: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: '请选择'
    }
  },
  data() {
    return {
      tips: this.placeholder,
      selectValues: []
    };
  },
  created() {
    this.readJson();
  },
  methods: {
    onOptionSelected(v) {
      this.$emit('onJsonValueSelected', v);
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    readJson() {
      getAction('json/list', { file: this.file, name: this.name, value: this.value })
        .then((res) => {
          if (res.success) {
            this.selectValues = res.result;
          } else {
            this.selectValues = [];
          }
        });
    },
    handleChange(value) {
      console.log(`selected ${value}`);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
