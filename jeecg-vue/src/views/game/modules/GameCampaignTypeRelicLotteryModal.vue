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
    <game-campaign-type-relic-lottery-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></game-campaign-type-relic-lottery-form>
  </j-modal>
</template>

<script>
import GameCampaignTypeRelicLotteryForm from './GameCampaignTypeRelicLotteryForm';
export default {
  name: 'GameCampaignTypeRelicLotteryModal',
  components: {
    GameCampaignTypeRelicLotteryForm
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
      // this.visible=true
      // this.$nextTick(()=>{
      //   this.$refs.realForm.add();
      // })
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