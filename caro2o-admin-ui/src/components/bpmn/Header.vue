<template>
  <div>
    <el-button-group>
      <!--<el-tooltip class="item" effect="dark" content="保存并发布" placement="bottom">
        <el-button type="primary" size="mini" @click="deploy"><i class="fa fa-save"> 保存并发布</i></el-button>
      </el-tooltip>-->
      <!--<el-tooltip class="item" effect="dark" content="保存草稿" placement="bottom">
        <el-button type="primary" size="mini" @click="save"><i class="fa fa-save"> 保存草稿</i></el-button>
      </el-tooltip>-->
      <!--<el-tooltip class="item" effect="dark" content="打开流程文件" placement="bottom">
        <el-button type="primary" size="mini"><i class="fa fa-folder-open"></i></el-button>
      </el-tooltip>-->
      <el-tooltip class="item" effect="dark" content="重置流程图" placement="bottom">
        <el-button type="primary" size="mini" @click="reset"><i class="fa fa-refresh"></i></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="下载流程图" placement="bottom">
        <el-button type="primary" size="mini" @click="downloadSvg"><i class="fa fa-picture-o"></i></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="下载流程文件" placement="bottom">
        <el-button type="primary" size="mini" @click="downloadBpmn"><i class="fa fa-download"></i></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="撤销" placement="bottom">
        <el-button size="mini"><i class="fa fa-rotate-left" @click="undo"></i></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="恢复" placement="bottom">
        <el-button size="mini"><i class="fa fa-rotate-right" :class="!canRedo?'default-undo':''"
                                  @click="redo"></i></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="放大" placement="bottom">
        <el-button size="mini" @click="zoom(0.05)"><i class="fa fa-search-plus"></i></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="缩小" placement="bottom">
        <el-button size="mini" @click="zoom(-0.05)"><i class="fa fa-search-minus"></i></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="重置" placement="bottom">
        <el-button size="mini" @click="zoom(0)"><i class="fa fa-arrows"></i></el-button>
      </el-tooltip>
    </el-button-group>
  </div>
</template>

<script>
  export default {
    name: "ViewerHeader",
    data() {
      return {
        scale: 1.0,
        canRedo: false
      }
    },
    props: {
      processData: {
        type: Object
      },
      modeler: {
        type: Object
      }
    },
    components: {},
    methods: {
      deploy() {
        let that = this;
        let _xml;
        let _svg;
        this.modeler.saveXML((err, xml) => {
          if (err) {
            console.error(err)
          }
          _xml = xml;
        })
        this.modeler.saveSVG((err, svg) => {
          if (err) {
            console.error(err)
          }
          _svg = svg;
        })
        console.log('TODO: 部署 bpmn 文件', {
          processKey: "bpmnkey",
          processName: "bpmn名称",
          resourceName: "资源名称",
          xml: _xml,
          svg: _svg
        })
      },
      save() {
        let that = this;
        let _xml;
        let _svg;
        this.modeler.saveXML((err, xml) => {
          if (err) {
            console.error(err)
          }
          _xml = xml;
        })
        this.modeler.saveSVG((err, svg) => {
          if (err) {
            console.error(err)
          }
          _svg = svg;
        })
        that.$emit("processSave", {"xmlStr": _xml, "svgStr": _svg});
      },
      reset() {
        this.$emit('restart')
      },
      downloadSvg() {
        this.$emit("handleExportSvg")
      },
      downloadBpmn() {
        this.$emit("handleExportBpmn");
      },
      undo() {
        this.modeler.get('commandStack').undo();
        this.canRedo = this.modeler.get('commandStack').canRedo();
      },
      redo() {
        if (!this.canRedo) {
          return;
        }
        this.modeler.get('commandStack').redo()
        this.canRedo = this.modeler.get('commandStack').canRedo();
      },
      zoom(val) {
        if (val === 0) {
          this.scale = 1;
          this.modeler.get('canvas').zoom('fit-viewport', 'auto');
        } else {
          let newScale = !val ? 1.0: ((this.scale + val) <= 0.2) ? 0.2: (this.scale + val);
          this.modeler.get('canvas').zoom(newScale);
          this.scale = newScale;
        }
      }
    }
  }
</script>

<style scoped>

</style>
