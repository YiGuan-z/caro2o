<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关键字" prop="goodsId">
        <el-input
          v-model="queryParams.goodsId"
          placeholder="请输入查找关键字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库" prop="storeId">
        <el-select v-model="queryParams.storeId" clearable>
          <el-option v-for="item in allStoreList"
                     :key="item.id"
                     :value="item.id"
                     :label="`${item['storeName']} ${item['storeAddress']}`"/>
        </el-select>
      </el-form-item>
      <el-form-item label="分类" prop="classify">
        <el-select v-model="queryParams.params['classify']" clearable>
          <el-option v-for="item in classify"
                     :value="item.id"
                     :label="item.categoryName"/>
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
          v-hasPermi="['store:store:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['store:store:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['store:store:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['store:store:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="storeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" prop="id"/>
      <el-table-column label="物品" align="center" prop="goodsId"/>
      <el-table-column label="名称" align="center" prop="goodsId"/>
      <el-table-column label="品牌" align="center" prop="goodsId"/>
      <el-table-column label="分类" align="center" prop="amounts"/>
      <el-table-column label="数量" align="center" prop="amounts"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['store:store:edit']"
          >编辑
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['store:store:remove']"
          >出入库明细
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改物品库存对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物品id" prop="goodsId">
          <el-input v-model="form.goodsId" placeholder="请输入物品id"/>
        </el-form-item>
        <el-form-item label="仓库id" prop="storeId">
          <el-input v-model="form.storeId" placeholder="请输入仓库id"/>
        </el-form-item>
        <el-form-item label="库存数量" prop="amounts">
          <el-input v-model="form.amounts" placeholder="请输入库存数量"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listStore, getStore, delStore, addStore, updateStore} from "@/api/store/goods_store";
import {listWarehouse} from '@/api/workflow/warehouse'
import {listCategory} from '@/api/workflow/category'

export default {
  name: "Store",
  data() {
    return {
      //分类的选择区
      classify: [],
      //所有仓库的选择区
      allStoreList: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 物品库存表格数据
      storeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        goodsId: null,
        storeId: null,
        amounts: null,
        params:{}
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    this.getAllStore()
    this.getAllCateGory()
  },
  methods: {
    //查询所有仓库
    async getAllStore() {
      const {rows} = await listWarehouse(null)
      this.allStoreList = rows
    },
    //listCategory
    async getAllCateGory() {
      const {data} = await listCategory(null)
      this.classify = data
      // console.log(rows)
      // const rootNodes = []
      // const cache = new Map()
      // data.forEach(node => {
      //   cache.set(node.busiPath, node)
      // })
      // cache.forEach(node => {
      //   //判断是否为顶级节点
      //   //顶级节点长度为1
      //   const nodeData = node.busiPath.split(':');
      //   const nodeLength = nodeData.length;
      //   if (nodeLength === 1) {
      //     rootNodes.push(node)
      //   } else {
      //     //如果不是顶级节点
      //     //通过减少子节点字符就可以拿到父节点的key
      //     const parentKey = node.busiPath.substring(0,node.busiPath.length-2);
      //     const parentNode = cache.get(parentKey);
      //     parentNode.children.push(node)
      //   }
      // })
      // this.classify=rootNodes
    },
    /** 查询物品库存列表 */
    getList() {
      this.loading = true;
      listStore(this.queryParams).then(response => {
        this.storeList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        goodsId: null,
        storeId: null,
        amounts: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加物品库存";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStore(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物品库存";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStore(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStore(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除物品库存编号为"' + ids + '"的数据项？').then(function () {
        return delStore(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('store/store/export', {
        ...this.queryParams
      }, `store_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
