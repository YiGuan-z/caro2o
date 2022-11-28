<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关键字" prop="goodsName">
        <el-input
          v-model="queryParams.params.keyword"
          placeholder="请输入关键字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库" prop="storeId">
        <el-select v-model="queryParams.params['storeId']" clearable>
          <el-option v-for="item in allStoreList"
                     :key="item.id"
                     :value="item.id"
                     :label="`${item['storeName']} ${item['storeAddress']}`"/>
        </el-select>
      </el-form-item>
      <el-form-item label="分类" prop="classify">
        <el-select v-model="queryParams.params['classify']" clearable>
          <el-option v-for="item in classify"
                     :key="item.id"
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
          v-hasPermi="['system:goods:add']"
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
          v-hasPermi="['system:goods:edit']"
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
          v-hasPermi="['system:goods:remove']"
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
          v-hasPermi="['system:goods:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" prop="id"/>
      <el-table-column label="物品名称" align="center" prop="goodsName"/>
      <el-table-column label="封面" align="center" prop="goodsCover">
        <template v-slot="scope">
          <div @click="handleViewImage(scope)">
            <el-avatar :src="scope.row.goodsCover"/>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="categoryId">
        <template v-slot="scope">
          {{scope.row.category.categoryName}}
        </template>
      </el-table-column>
      <el-table-column label="品牌" align="center" prop="brand"/>
      <el-table-column label="规格" align="center" prop="spec"/>
      <el-table-column label="描述" align="center" prop="goodsDesc"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:goods:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:goods:remove']"
          >删除
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

    <!-- 添加或修改物品信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物品名称" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="请输入物品名称"/>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" clearable>
            <el-option v-for="item in classify"
                       :key="item.id"
                       :value="item.id"
                       :label="item.categoryName"/>
          </el-select>
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌"/>
        </el-form-item>
        <el-form-item label="图片" prop="goodsCover">
          <el-upload
            class="avatar-uploader"
            action="/dev-api/common/upload1"
            :headers="headler"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <el-image v-if="form.goodsCover"
                      :src="form.goodsCover"
                      class="avatar"/>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="描述" prop="goodsDesc">
          <el-input type="textarea" v-model="form.goodsDesc" placeholder="请输入描述"/>
        </el-form-item>

        <el-form-item label="规格" prop="spec">
          <el-input v-model="form.spec" placeholder="请输入规格"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="imageVisible">
      <el-image width="100%" :src="viewImage" alt=""/>
    </el-dialog>
  </div>
</template>

<script>
import {listGoods, getGoods, delGoods, addGoods, updateGoods} from "@/api/store/goods";
import {getToken} from "@/utils/auth";
import {listCategory} from "@/api/workflow/category";
import {listWarehouse} from "@/api/workflow/warehouse";

export default {
  name: "Goods",
  data() {
    return {
      //所有仓库的选择区
      allStoreList: [],
      classify: [],
      headler: {
        Authorization: `Bearer ${getToken()}`
      },
      viewImage: null,
      imageVisible: false,
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
      // 物品信息表格数据
      goodsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        goodsName: null,
        brand: null,
        spec: null,
        goodsDesc: null,
        params:{}
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        goodsName: [
          {required: true, message: "物品名称不能为空", trigger: "blur"}
        ],
        goodsCover: [
          {required: true, message: "封面不能为空", trigger: "blur"}
        ],
        categoryId: [
          {required: true, message: "分类不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getAllStore()
    this.getAllCateGory()
  },
  methods: {
    async getAllStore() {
      const {rows} = await listWarehouse(null)
      this.allStoreList = rows
    },
    async getAllCateGory() {
      const {data} = await listCategory(null)
      this.classify = data;
    },
    handleViewImage({row}) {
      const {goodsCover} = row
      this.viewImage = goodsCover
      this.imageVisible = true
    },
    //文件上传成功操作
    handleAvatarSuccess(res, file) {
      console.log(res)
      const {url} = res
      this.form.goodsCover = url
      // this.form.goodsCover = URL.createObjectURL(file.raw);
    },
    //文件上传之前的检查操作
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt5m = file.size / 1024 / 1024 < 5;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt5m) {
        this.$message.error('上传头像图片大小不能超过 5MB!');
      }
      return isJPG && isLt5m;
    },
    /** 查询物品信息列表 */
    getList() {
      this.loading = true;
      listGoods(this.queryParams).then(response => {
        this.goodsList = response.rows;
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
        goodsName: null,
        goodsCover: null,
        categoryId: null,
        brand: null,
        spec: null,
        goodsDesc: null
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
      this.title = "添加物品信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getGoods(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物品信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateGoods(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGoods(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除物品信息编号为"' + ids + '"的数据项？').then(function () {
        return delGoods(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/goods/export', {
        ...this.queryParams
      }, `goods_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
