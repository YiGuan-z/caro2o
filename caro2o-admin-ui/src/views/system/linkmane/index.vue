<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="联系人" prop="linkman">
        <el-input
          v-model="queryParams.linkman"
          placeholder="请输入联系人名字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系人电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系人电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任职状态" prop="positionState">
        <el-select v-model="queryParams.positionState" placeholder="请选择任职状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_position_state"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          v-hasPermi="['system:linkmane:add']"
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
          v-hasPermi="['system:linkmane:edit']"
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
          v-hasPermi="['system:linkmane:remove']"
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
          v-hasPermi="['system:linkmane:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="linkmaneList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" prop="id"/>
      <el-table-column label="客户" align="center" prop="customer.legalLeader"/>
      <el-table-column label="联系人名字" align="center" prop="linkman"/>
      <el-table-column label="性别" align="center" prop="gender">
        <template v-slot="scope">
            {{scope.row.gender == 1 ? '男' : '女'}}
        </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="age"/>
      <el-table-column label="联系人电话" align="center" prop="phone"/>
      <el-table-column label="职位" align="center" prop="position"/>
      <el-table-column label="任职状态" align="center" prop="positionState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_position_state" :value="scope.row.positionState"/>
        </template>
      </el-table-column>
      <el-table-column label="部门" align="center" prop="department"/>
      <el-table-column label="录入人" align="center" prop="inputUserName"/>
      <el-table-column label="录入时间" align="center" prop="inputTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.inputTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:linkmane:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:linkmane:remove']"
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

    <!-- 添加或修改客户联系人对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col span="12">
            <el-form-item label="所属客户" prop="customer">
              <el-select v-model="form.customer" placeholder="请选择">
                <el-option
                  v-for="item in customers"
                  :key="item.id"
                  :label="item.legalLeader"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="联系人名字" prop="linkman">
              <el-input v-model="form.linkman" placeholder="请输入联系人名字"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="性别" prop="gender">
              <template>
                <el-radio v-model="form.gender" :label="1">男</el-radio>
                <el-radio v-model="form.gender" :label="0">女</el-radio>
              </template>
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="年龄" prop="age">
              <el-input type="number" v-model="form.age" placeholder="请输入年龄"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="联系人电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系人电话"/>
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="职位" prop="position">
              <el-input v-model="form.position" placeholder="请输入职位"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="任职状态" prop="positionState">
              <el-select v-model="form.positionState" placeholder="请选择任职状态">
                <el-option
                  v-for="dict in dict.type.sys_position_state"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="部门" prop="department">
              <el-input v-model="form.department" placeholder="请输入部门"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listLinkmane, getLinkmane, delLinkmane, addLinkmane, updateLinkmane} from "@/api/system/linkmane";
  import {listAll} from "../../../api/system/customer";

  export default {
    name: "Linkmane",
    dicts: ['sys_position_state'],
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
        // 客户联系人表格数据
        linkmaneList: [],
        // 弹出层标题
        title: "",

        customers:[],
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          customer: null,
          linkman: null,
          phone: null,
          positionState: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          customer: [
            {required: true, message: "客户不能为空", trigger: "blur"}
          ],
          linkman: [
            {required: true, message: "联系人不能为空", trigger: "blur"}
          ],
          gender: [
            {required: true, message: "性别必填", trigger: "blur"}
          ],
          phone: [
            {required: true, message: "电话不能为空", trigger: "blur"}
          ],
          positionState: [
            {required: true, message: "任职状态不能为空", trigger: "blur"}
          ],
        }
      };
    },
    created() {
      this.getList();
      this.getCustomer();
    },
    methods: {

      //查询客户
      getCustomer(){
        listAll().then(res =>{
          this.customers = res.data
        })
      },

      /** 查询客户联系人列表 */
      getList() {
        this.loading = true;
        listLinkmane(this.queryParams).then(response => {
          this.linkmaneList = response.rows;
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
          customer: null,
          linkman: null,
          gender: null,
          age: null,
          phone: null,
          position: null,
          positionState: null,
          department: null,
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
        this.title = "添加客户联系人";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getLinkmane(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改客户联系人";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateLinkmane(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addLinkmane(this.form).then(response => {
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
        this.$modal.confirm('是否确认删除客户联系人编号为"' + ids + '"的数据项？').then(function () {
          return delLinkmane(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/linkmane/export', {
          ...this.queryParams
        }, `linkmane_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
