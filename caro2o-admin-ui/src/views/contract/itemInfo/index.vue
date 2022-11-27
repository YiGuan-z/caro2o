<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="审核状态" prop="auditState">
        <el-select v-model="queryParams.auditState" placeholder="请选择审核状态" clearable>
          <el-option
            v-for="dict in dict.type.item_audit_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="录入时间" prop="inputTime">
        <el-date-picker clearable
                        v-model="queryParams.inputTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择录入时间">
        </el-date-picker>
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
          v-hasPermi="['contract:itemInfo:add']"
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
          v-hasPermi="['contract:itemInfo:edit']"
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
          v-hasPermi="['contract:itemInfo:remove']"
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
          v-hasPermi="['contract:itemInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="itemInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id"/>
      <el-table-column label="客户id" align="center" prop="customerId"/>
      <el-table-column label="合同名称" align="center" prop="contractName"/>
      <el-table-column label="合同编号" align="center" prop="contractCode"/>
      <el-table-column label="合同金额" align="center" prop="amounts"/>
      <el-table-column label="合同生效开始时间" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同失效时间" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="附件" align="center" prop="appendix"/>
      <el-table-column label="是否盖章确认" align="center" prop="affixSealState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.item_confirm_status" :value="scope.row.affixSealState"/>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="auditState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.item_audit_status" :value="scope.row.auditState"/>
        </template>
      </el-table-column>
      <el-table-column label="是否作废" align="center" prop="nullifyState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.item_use_status" :value="scope.row.nullifyState"/>
        </template>
      </el-table-column>
      <el-table-column label="录入人" align="center" prop="inputUser"/>
      <el-table-column label="录入时间" align="center" prop="inputTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.inputTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['contract:itemInfo:edit']"
          >编辑
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.nullifyState !== -1 "
            @click="handlePass(scope.row)"
            v-hasPermi="['contract:itemInfo:edit']"
          >审核通过
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.nullifyState !== -1 || scope.row.affixSealState === 0"
            @click="auditConfirm(scope.row)"
            v-hasPermi="['contract:itemInfo:edit']"
          >确认盖章
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.nullifyState !== -1"
            @click="auditReject(scope.row)"
            v-hasPermi="['contract:itemInfo:edit']"
          >审核不通过
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.nullifyState !== -1"
            @click="failure(scope.row)"
            v-hasPermi="['contract:itemInfo:remove']"
          >作废
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

    <!-- 添加或修改合同项信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-form-item label="客户名称" prop="customerId">
                <el-select v-model="form.customerId" placeholder="请选择客户">
                  <el-option
                    v-for="item in customerList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light">
              <el-form-item label="合同名称" prop="contractName">
                <el-input v-model="form.contractName" placeholder="请输入合同名称"/>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-form-item label="合同编号" prop="contractCode">
                <el-input v-model="form.contractCode" placeholder="请输入合同编号"/>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light">
              <el-form-item label="合同金额" prop="amounts">
                <el-input v-model="form.amounts" placeholder="请输入合同金额"/>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple"></div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light"></div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-form-item label="开始时间" prop="startDate">
                <el-date-picker clearable
                                v-model="form.startDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择合同生效开始时间">
                </el-date-picker>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light">
              <el-form-item label="失效时间" prop="endDate">
                <el-date-picker clearable
                                v-model="form.endDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择合同失效时间">
                </el-date-picker>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-form-item label="附件">
          <file-upload v-model="form.appendix"/>
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
import {
  listItemInfo,
  getItemInfo,
  delItemInfo,
  addItemInfo,
  updateItemInfo,
  auditPass,
  auditConfirm,
  auditReject,
  failure
} from "@/api/contract/itemInfo";
import {listAll} from "@/api/system/customer";
import log from "@/views/monitor/job/log";

export default {
  name: "ItemInfo",
  dicts: ['item_confirm_status', 'item_use_status', 'item_audit_status'],
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
      // 合同项信息表格数据
      itemInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 客户列表
      customerList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        auditState: null,
        inputTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    listAll().then(resp => {
      let temp = resp.data.map(u => ({value:u.id, label:u.legalLeader}))
      this.customerList = temp;
      console.log(this.customerList);
    })
    this.getList();
  },
  methods: {
    /** 查询合同项信息列表 */
    getList() {
      this.loading = true;
      listItemInfo(this.queryParams).then(response => {
        this.itemInfoList = response.rows;
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
        contractName: null,
        contractCode: null,
        amounts: null,
        startDate: null,
        endDate: null,
        appendix: null,
        affixSealState: null,
        auditState: null,
        nullifyState: null,
        inputUser: null,
        inputTime: null
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
      this.title = "添加合同项信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getItemInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改合同项信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateItemInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addItemInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认废除合同项信息编号为"' + ids + '"的数据项？').then(function () {
        return delItemInfo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("作废成功");
      }).catch(() => {
      });
    },
    /** 审核通过按钮操作 */
    handlePass(row) {
      this.$modal.confirm('是否确认通过合同项信息编号为"' + ids + '"的数据项？').then(function () {
        return auditPass(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("审核通过");
      }).catch(() => {
      });
    },
    /** 审核拒绝按钮操作 */
    auditReject(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认拒绝合同项信息编号为"' + ids + '"的数据项？').then(function () {
        return auditReject(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("审核拒绝");
      }).catch(() => {
      });
    },
    /** 确认盖章按钮操作 */
    auditConfirm(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认合同项信息编号为"' + ids + '"的数据项？').then(function () {
        return auditConfirm(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("确认盖章");
      }).catch(() => {
      });
    },
    /** 作废按钮操作 */
    failure(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认合同项信息编号为"' + ids + '"的数据项？').then(function () {
        return failure(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("确认盖章");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('contract/itemInfo/export', {
        ...this.queryParams
      }, `itemInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
