<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="法定代表人" prop="legalLeader">
        <el-input
          v-model="queryParams.legalLeader"
          placeholder="请输入法定代表人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经营状态" prop="openState">
        <el-select v-model="queryParams.openState" placeholder="请选择经营状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_customer_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属省份" prop="province">
        <el-select v-model="form.province" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_city_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所属行业" prop="industry">
        <el-input
          v-model="queryParams.industry"
          placeholder="请输入所属行业"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['system:customer:add']"
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
          v-hasPermi="['system:customer:edit']"
        >修改
        </el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="danger"-->
      <!--          plain-->
      <!--          icon="el-icon-delete"-->
      <!--          size="mini"-->
      <!--          :disabled="multiple"-->
      <!--          @click="handleDelete"-->
      <!--          v-hasPermi="['system:customer:remove']"-->
      <!--        >删除-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:customer:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" prop="id"/>
      <el-table-column label="企业名称" align="center" prop="customerName"/>
      <el-table-column label="法定代表人" align="center" prop="legalLeader"/>
      <el-table-column label="成立时间" align="center" prop="registerDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registerDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="经营状态" align="center" prop="openState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_customer_status" :value="scope.row.openState"/>
        </template>
      </el-table-column>
      <el-table-column label="所属省份" align="center" prop="province">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_city_status" :value="scope.row.province"/>
        </template>
      </el-table-column>
      <el-table-column label="注册资本,(万元)" align="center" prop="regCapital"/>
      <el-table-column label="所属行业" align="center" prop="industry"/>
      <el-table-column label="经营范围" align="center" prop="scope"/>
      <el-table-column label="注册地址" align="center" prop="regAddr"/>
      <el-table-column label="录入人" align="center" prop="inputUserName"/>
      <el-table-column label="营销人" align="center" prop="userMarket"/>
      <el-table-column label="录入时间" align="center" prop="inputTime" width="180">
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:customer:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-info"
            @click="handleSelect(scope.row)"
            v-hasPermi="['system:customer:selectById']"
          >详情
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

    <el-dialog :title="title" :visible.sync="opens" width="700px" append-to-body>
      <el-form ref="form" :model="selectInfo" label-width="100px">
        <el-descriptions title="客户信息">
          <el-descriptions-item label="企业名称">
            {{selectInfo.customerName}}
          </el-descriptions-item>
          <el-descriptions-item label="法定代表人">
            {{selectInfo.legalLeader}}
          </el-descriptions-item>
          <el-descriptions-item label="成立时间">
            {{selectInfo.registerDate}}
          </el-descriptions-item>
          <el-descriptions-item label="经营状态">
            <dict-tag :options="dict.type.sys_customer_status" :value="selectInfo.openState"/>
          </el-descriptions-item>
          <el-descriptions-item label="所属省份">
            <dict-tag :options="dict.type.sys_city_status" :value="selectInfo.openState"/>
          </el-descriptions-item>
          <el-descriptions-item label="注册资本">
            {{selectInfo.regCapital}}
          </el-descriptions-item>
          <el-descriptions-item label="所属行业">
            {{selectInfo.industry}}
          </el-descriptions-item>
          <el-descriptions-item label="经营范围">
            {{selectInfo.scope}}
          </el-descriptions-item>
          <el-descriptions-item label="注册地址">
            {{selectInfo.regAddr}}
          </el-descriptions-item>
        </el-descriptions>
      </el-form>
    </el-dialog>

    <!-- 添加或修改客户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业名称" prop="customerName">
              <el-input v-model="form.customerName" placeholder="请输入企业名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法定代表人" prop="legalLeader">
              <el-input v-model="form.legalLeader" placeholder="请输入法定代表人"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="成立时间" prop="registerDate">
              <el-date-picker clearable
                              v-model="form.registerDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择成立时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经营状态" prop="openState">
              <el-select v-model="form.openState" placeholder="请选择经营状态" clearable>
                <el-option
                  v-for="dict in dict.type.sys_customer_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="所属省份" prop="province">
              <el-select v-model="form.province" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_city_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业" prop="industry">
              <el-input v-model="form.industry" placeholder="请输入所属行业"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="注册资本" prop="regCapital">
          <el-input type="number" v-model="form.regCapital" placeholder="请输入注册资本,(万元)"/>
        </el-form-item>

        <el-form-item label="经营范围" prop="scope">
          <el-input v-model="form.scope" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="注册地址" prop="regAddr">
          <el-input v-model="form.regAddr" type="textarea" placeholder="请输入内容"/>
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
  import {listCustomer, getCustomer, delCustomer, addCustomer, updateCustomer} from "@/api/system/customer";

  export default {
    name: "Customer",
    dicts: ['sys_customer_status', 'sys_city_status'],
    data() {
      return {
        selectInfo: {},
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
        // 客户信息表格数据
        customerList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        opens: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          legalLeader: null,
          openState: null,
          province: null,
          industry: null,
        },
        // 表单参数
        form: {},
        particulars: {},
        // 表单校验
        rules: {
          customerName: [
            {required: true, message: "客户名称不能为空", trigger: "blur"}
          ],
          legalLeader: [
            {required: true, message: "法定代表人不能为空", trigger: "blur"}
          ],
          registerDate: [
            {required: true, message: "时间不能为空", trigger: "blur"}
          ],
          openState: [
            {required: true, message: "状态不能为空", trigger: "blur"}
          ],
          province: [
            {required: true, message: "所属省份不能为空", trigger: "blur"}
          ],
          industry: [
            {required: true, message: "所属行业不能为空", trigger: "blur"}
          ],
          regCapital: [
            {required: true, message: "注册资金不能为空", trigger: "blur"}
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {


      /** 查询客户信息列表 */
      getList() {
        this.loading = true;
        listCustomer(this.queryParams).then(response => {
          this.customerList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.opens = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          customerName: null,
          legalLeader: null,
          registerDate: null,
          openState: null,
          province: null,
          regCapital: null,
          industry: null,
          scope: null,
          regAddr: null,
          inputTime: null,
          inputUser: null
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
        this.title = "添加客户信息";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getCustomer(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改客户信息";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateCustomer(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addCustomer(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      //详情按钮查看数据
      handleSelect(row) {
        this.opens = true;
        this.selectInfo = {...row}
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/customer/export', {
          ...this.queryParams
        }, `customer_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
