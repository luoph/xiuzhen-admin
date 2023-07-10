<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <game-campaign-type-lottery-token-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></game-campaign-type-lottery-token-form>
  </j-modal>
</template>

<script>
import GameCampaignTypeLotteryTokenForm from './GameCampaignTypeLotteryTokenForm';
export default {
  name: 'GameCampaignTypeLotteryTokenModal',
  components: {
    GameCampaignTypeLotteryTokenForm
  },
  data() {
    return {
      title: '',
      width: 800,
      visible: false,
      disableSubmit: false
    };
  },
  methods: {
    add(record) {
      this.edit(record);
    },
    edit(record) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.realForm.edit(record);
      });
    },
    close() {
      this.$emit('close');
      this.visible = false;
    },
    handleOk() {
      this.$refs.realForm.submitForm();
    },
    submitCallback() {
      this.$emit('ok');
      this.visible = false;
    },
    handleCancel() {
      this.close();
    }
  }
};
</script>