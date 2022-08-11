<template>
  <a-row class="j-select-biz-component-box" type="flex" :gutter="8">
    <a-col class="full" :class="{ full: !buttons }">
      <a-input :placeholder="placeholder" :disabled="disabled" style="width: 100%" v-model="selectValue" allowClear>
        <a-icon slot="addonAfter" type="setting" @click="visible = true" />
      </a-input>
    </a-col>

    <game-image-list-modal v-model="selectValue" :name="name" :returnKeys="returnKeys" :visible.sync="visible" :valueKey="valueKey" @ok="selectOptions = $event" />
  </a-row>
</template>

<script>
import GameImageListModal from './GameImageListModal';

export default {
  name: 'GameImageComponent',
  components: { GameImageListModal },
  props: {
    value: {
      type: String,
      default: ''
    },
    /** 是否返回 id，默认 false，返回 code */
    returnId: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: '请选择'
    },
    disabled: {
      type: Boolean,
      default: false
    },
    // 是否显示按钮，默认 true
    buttons: {
      type: Boolean,
      default: true
    },
    /* 可复用属性 */
    name: {
      type: String,
      default: ''
    },
    // 显示的 Key
    displayKey: {
      type: String,
      default: null
    },
    // 返回的 key
    returnKeys: {
      type: Array,
      default: () => ['id', 'id']
    },
    // 选择按钮文字
    selectButtonText: {
      type: String,
      default: '选择'
    }
  },
  data() {
    return {
      selectValue: '',
      selectOptions: [],
      visible: false
    };
  },
  computed: {
    valueKey() {
      return this.returnId ? this.returnKeys[0] : this.returnKeys[1];
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        if (val) {
          this.selectValue = val;
        } else {
          this.selectValue = '';
        }
      }
    },
    selectValue: {
      deep: true,
      handler(val) {
        const data = val;
        this.$emit('input', data);
        this.$emit('change', data);
      }
    }
  },
  methods: {}
};
</script>

<style lang="less">
.j-select-biz-component-box {
  .ant-select-search__field {
    display: none !important;
  }
}
</style>
<style lang="less" scoped>
.j-select-biz-component-box {
    width: 40px;

    .left {
        width: calc(100% - 40 - 8px);
    }

    .right {
        width: 40;
    }

    .full {
        width: 100%;
    }
}
</style>
