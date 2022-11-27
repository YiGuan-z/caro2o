<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关键字" prop="keyword">
        <el-input v-model="queryParams.keyword" placeholder="请输入关键字" clearable/>
      </el-form-item>
      <el-form-item label="拜访方式" prop="visitType">
        <el-select v-model="queryParams.visitType" placeholder="请选择拜访方式" clearable>
          <el-option
            v-for="dict in dict.type.bus_customer_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="拜访时间" prop="visitDate">
        <div class="block">
          <span>
            <el-date-picker
              v-model="queryParams.startVisitDate"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
          </el-date-picker> -
          <el-date-picker
            v-model="queryParams.endVisitDate"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
          </span>
        </div>
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
          v-hasPermi="['customer:visit:add']"
        >新增
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="visitList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="主键id" align="center" prop="id"/>
      <el-table-column label="客户id" align="center" prop="customerId"/>
      <el-table-column label="联系人id" align="center" prop="linkmanId"/>
      <el-table-column label="拜访方式" align="center" prop="visitType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_customer_type" :value="scope.row.visitType"/>
        </template>
      </el-table-column>
      <el-table-column label="拜访原因" align="center" prop="visitReason" show-overflow-tooltip/>
      <el-table-column label="交流内容" align="center" prop="content" show-overflow-tooltip/>
      <el-table-column label="拜访时间" align="center" prop="visitDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="录入人" align="center" prop="inputUser"/>
      <el-table-column label="录入时间" align="center" prop="inputTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.inputTime, '{y}-{m}-{d}') }}</span>
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

    <!-- 添加或修改拜访信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="95px">
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-form-item label="拜访客户" prop="customerId">
                <el-select v-model="form.customerId" placeholder="请选择客户">
                  <el-option
                    v-for="list in customerList"
                    :key="list.value"
                    :label="list.label"
                    :value="parseInt(list.value)"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light">
              <el-form-item label="客户联系人" prop="linkmanId">
                <el-select v-model="form.linkmanId" placeholder="请选择联系人">
                  <el-option
                    v-for="concat in concatList"
                    :key="concat.value"
                    :label="concat.label"
                    :value="parseInt(concat.value)"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-form-item label="拜访方式" prop="visitType">
                <el-select v-model="form.visitType" placeholder="请选择拜访方式">
                  <el-option
                    v-for="dict in dict.type.bus_customer_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light">
              <el-form-item label="拜访时间" prop="visitDate">
                <el-date-picker clearable
                                v-model="form.visitDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择拜访日期">
                </el-date-picker>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-form-item label="拜访原因" prop="visitReason">
                <el-input v-model="form.visitReason" type="textarea" placeholder="请输入内容"/>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light"></div>
          </el-col>
        </el-row>
        <el-form-item label="交流内容" prop="content">
          <el-input v-model="form.content" type="textarea" placeholder="请输入内容"/>
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
import {listVisit, addVisit, updateVisit} from "@/api/customer/visit";

export default {
  name: "Visit",
  dicts: ['bus_customer_type'],
  data() {
    return {
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
      // 拜访信息表格数据
      visitList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 客户列表
      customerList: [{value:1, label:"老黑"}],
      // 联系人列表
      concatList: [{value:1, label:"小黑"}],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        visitType: null,
        visitReason: null,
        startVisitDate: null,
        endVisitDate: null,
        keyword: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerId: [
          {required: true, message: '客户不能为空', trigger: 'blur'}
        ],
        linkmanId: [
          {required: true, message: '联系人不能为空', trigger: 'blur'}
        ],
        visitType: [
          {required: true, message: '拜访方式不能为空', trigger: 'blur'}
        ],
        visitDate: [
          {required: true, message: '交流情况不能为空', trigger: 'blur'}
        ],
        visitReason: [
          {required: true, message: '拜访元婴不嫩为空', trigger: 'blur'}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询拜访信息列表 */
    getList() {
      this.loading = true;
      listVisit(this.queryParams).then(response => {
        this.visitList = response.rows;
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
        customerId: null,
        linkmanId: null,
        visitType: null,
        visitReason: null,
        content: null,
        visitDate: null,
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
      this.title = "添加拜访信息";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateVisit(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVisit(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
