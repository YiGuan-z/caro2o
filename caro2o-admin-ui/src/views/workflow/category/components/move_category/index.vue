<template>
  <div class="app">
    <el-form :model="form" size="small" :inline="true" label-width="80px" center>
      <el-row :span="6">
        <el-form-item label="被迁移的分类">
          <el-select v-model="form.from" placeholder="请选择" ref="insertTree" clearable>
            <el-option :key="form.from"
                       :value="form.from"
                       :label="form.fromLabel"
                       hidden/>
            <el-tree :data="treeList"
                     :props="defaultProps"
                     node-key="id"
                     :default-expand-all="true"
                     :expand-on-click-node="false"
                     clearable
                     @node-click="fromNodeCheck"/>
          </el-select>
        </el-form-item>
      </el-row>
      <el-row :span="6">
        <el-form-item label="迁移到">
          <el-select v-model="form.to" placeholder="请选择" ref="insertTree" clearable>
            <el-option :key="form.to"
                       :value="form.to"
                       :label="form.toLabel"
                       hidden/>
            <el-tree :data="treeList"
                     :props="defaultProps"
                     node-key="id"
                     :default-expand-all="true"
                     :expand-on-click-node="false"
                     clearable
                     @node-click="toNodeCheck"/>
          </el-select>
        </el-form-item>
      </el-row>
      <el-row :span="6">
        <el-form-item label="包括子分类">
          <el-checkbox v-model="form.son"/>
        </el-form-item>
      </el-row>
      <el-row :span="6">
        <el-button type="success">提交</el-button>
      </el-row>
    </el-form>

  </div>

</template>

<script>
import {listCategoryTree} from '@/api/workflow/category'
import Treeselect from "@riophae/vue-treeselect";

export default {
  components: {
    Treeselect
  },
  name: "moveCategory",
  data() {
    return {
      treeList: [],
      form: {
        from: null,
        fromLabel: null,
        to: null,
        toLabel: null,
        son: null
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      },
    }
  },
  created() {
    this.getTreeList()
  },
  methods: {
    async getTreeList() {
      const {data} = await listCategoryTree();
      this.treeList = data;
    },
    fromNodeCheck(data) {
      this.form.from = data.id;
      this.form.fromLabel = data.label;
      this.$refs.insertTree.blur();
    },
    toNodeCheck(data) {
      const {id, label} = data;
      this.form.to = id;
      this.form.toLabel = label
    }
  }
}
</script>

<style scoped>

</style>
