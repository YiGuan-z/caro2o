<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分类名称" prop="categoryName">
        <el-input
          v-model="queryParams.categoryName"
          placeholder="请输入分类名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上级分类">
        <el-select v-model="queryParams.parentId" placeholder="请选择" ref="insertTree" clearable>
          <el-option :key="queryParams.parent.id"
                     :value="queryParams.parentId"
                     :label="queryParams.parent.categoryName"
                     hidden/>
          <el-tree :data="treeList"
                   :props="defaultProps"
                   node-key="id"
                   :default-expand-all="true"
                   :expand-on-click-node="false"
                   clearable
                   @node-click="insertNodeClick"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['workflow:category:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="moveViewOpen=true"
        >分类移动
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="categoryList"
      row-key="id"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="序号" prop="id"/>
      <el-table-column label="上级分类" prop="parent.id"/>
      <el-table-column label="分类" align="center" prop="busiPath"/>
      <el-table-column label="概述" align="center" prop="categoryDesc"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['workflow:category:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:category:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改物品分类信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.label" placeholder="请输入分类名称"/>
        </el-form-item>
        <el-form-item label="描述" prop="categoryDesc">
          <el-input v-model="form.categoryDesc" placeholder="请输入描述"/>
        </el-form-item>
        <el-form-item label="上级分类">
          <el-select v-model="queryParams.parentId" placeholder="请选择" ref="insertTree">
            <el-option :key="queryParams.parent.id" :value="queryParams.parentId"
                       :label="queryParams.parent.categoryName"
                       hidden/>
            <el-tree :data="treeList"
                     :props="defaultProps"
                     node-key="id"
                     :default-expand-all="true"
                     :expand-on-click-node="false"
                     clearable
                     @node-click="insertNodeClick"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="物品移动" :visible.sync="moveViewOpen" width="1000px" append-to-body>
      <MoveCategory/>
    </el-dialog>
  </div>
</template>

<script>
import {
  addCategory,
  delCategory,
  getCategory,
  listCategory,
  updateCategory,
  getTreeDate,
  listCategoryTree,
  getCategoryTreeById
} from "@/api/workflow/category";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import move_category from "@/views/workflow/category/components/move_category";
import {listMenu} from "@/api/system/menu";

export default {
  name: "Category",
  components: {
    Treeselect,
    MoveCategory:move_category
  },
  data() {
    return {
      moveViewOpen:false,
      // 树结构分类
      treeList: [],
      form: {
        parent: {
          id: null,
          categoryName: null
        },
        params: {}
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 物品分类信息表格数据
      categoryList: [],
      // 物品分类信息树选项
      categoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        categoryName: null,
        categoryDesc: null,
        busiPath: null,
        parentid: null,
        parent: {
          id: null,
          categoryName: null
        },
        params: {}
      },
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    this.getTreeList();
  },
  watch: {
    // 根据名称筛选仓库树
    filterName(val) {
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    async getTreeList() {
      const {data} = await listCategoryTree();
      this.treeList = data;
    },
    insertNodeClick(data) {
      this.queryParams.parent.id = data.id
      this.queryParams.parent.categoryName = data.label
      // console.log(this.queryParams.parent.categoryName)
      // 使 input 失去焦点，并隐藏下拉框
      this.$refs.insertTree.blur()
    },
    /** 查询物品分类信息列表 */
    getList() {
      this.loading = true;

      listCategory(this.queryParams).then(response => {
        this.categoryList = this.handleTree(response.data, "id", "busiPath");
        this.loading = false;
      });
    },
    /** 转换物品分类信息数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.categoryName,
        children: node.children
      };
    },
    /** 查询物品分类信息下拉树结构 */
    getTreeselect() {
      listCategory().then(response => {
        this.categoryOptions = [];
        const data = {id: 0, categoryName: '顶级节点', children: []};
        data.children = this.handleTree(response.data, "id", "busiPath");
        this.categoryOptions.push(data);
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        categoryName: null,
        categoryDesc: null,
        busiPath: null,
        parentId: null,
      };
      this.queryParams={
        categoryName: null,
        categoryDesc: null,
        busiPath: null,
        parentid: null,
        parent: {
          id: null,
          categoryName: null
        },
        params: {}
      }
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.reset()
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.id) {
        this.form.busiPath = row.id;
      } else {
        this.form.busiPath = 0;
      }
      this.open = true;
      this.title = "添加物品分类信息";
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      console.log(row)
      if (row != null) {
        this.form.busiPath = row.id;
        this.form.categoryName=row.label
      }
      getCategory(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物品分类信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          Object.keys(this.form.parent).forEach(key=>this.form.parent[key]=null)
          // this.form.parent.id=this.queryParams.parent.id;
          this.form.params.parentId=this.queryParams.parent.id
          this.form.categoryName=this.form.label
          delete this.form.parent
          console.log(this.form)
          if (this.form.id != null) {
            updateCategory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCategory(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除物品分类信息编号为"' + row.id + '"的数据项？').then(function () {
        return delCategory(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    }
  }
};
</script>
