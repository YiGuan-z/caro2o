<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="出入库时间">
        <el-date-picker
          v-model="daterangeBusiDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable>
          <el-option
            v-for="dict in dict.type.sb_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sb_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仓库" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择仓库" clearable>
          <el-option
            v-for="dict in dict.type.sb_status"
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
          v-hasPermi="['store:bill:add']"
        >入库
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handleUpdate"
          v-hasPermi="['store:bill:edit']"
        >出库
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="billList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" prop="id"/>
      <el-table-column label="仓库" align="center" prop="storeId">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sb_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="仓库" align="center" prop="storeName"/>
      <el-table-column label="总数量" align="center" prop="amounts"/>
      <el-table-column label="总金额" align="center" prop="price"/>
      <el-table-column label="录入人" align="center" prop="createBy"/>
      <el-table-column label="出入库时间" align="center" prop="busiDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.busiDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="录入时间" align="center" prop="operateDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operateDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sb_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['store:bill:edit']"
            v-if="scope.row.status==0"
          >作废
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="itemQuery(scope.row.id)"
            v-hasPermi="['store:bill:remove']"
          >查看
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

    <!-- 添加或修改出入库单据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="id" prop="id" hidden/>
        <el-form-item label="仓库" prop="storeId">
          <el-select v-model="form.storeId" placeholder="请选择类型" :disabled="disabled">
            <el-option
              v-for="dict in storeList"
              :key="dict.id"
              :label="dict.storeName"
              :value="dict.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出入库时间" prop="busiDate">
          <el-date-picker clearable
                          v-model="form.busiDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          :picker-options="expireTimeOption"
                          placeholder="请选择出入库时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="5" style="width:40%" placeholder="请输入内容"/>
        </el-form-item>
        <el-row :gutter="10" class="mb8" v-if="onoff">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAddGoods"
              v-hasPermi="['store:bill:add']"
            >添加明细
            </el-button>
          </el-col>
        </el-row>

        <el-table
          :data="form.itemFrom"
          border
          style="width: 1000px">
          <el-table-column
            fixed
            prop="id"
            label="序号"
            width="150">
          </el-table-column>
          <el-table-column
            prop="goodsCover"
            label="物品"
            width="120">
            <template v-slot="scope">
              <el-image v-if="scope.row.goodsCover"
                        :src="scope.row.goodsCover"
                        class="avatar"/>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              <el-tag type="message">{{scope.row.goodsName}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="price"
            label="价格"
            width="120">
            <template slot-scope="scope">
              <el-form-item :prop="'itemFrom.'+scope.$index+'.price'" label-width="0px">
                <el-input @change="numChange(scope.row)" v-model="scope.row.price" type="text"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            prop="amounts"
            label="数量"
            width="120">
            <template slot-scope="scope">
              <el-form-item :prop="'itemFrom.'+scope.$index+'.amounts'" label-width="0px" :rules="rules.price">
                <el-input @change="numChange(scope.row)" v-model="scope.row.amounts" type="text"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            prop="sum"
            label="小计"
            width="300"
          >
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            v-if="onoff"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="itemDelete(scope.row)"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-if="onoff">确 定</el-button>
        <el-button @click="cancel">关闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openTwo" width="1000px" append-to-body>
        <el-row :gutter="10" class="mb8" v-if="onoff">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['store:bill:add']"
            >添加物品
            </el-button>
          </el-col>
        </el-row>

        <el-table
          :data="goodsList"
          border
          style="width: 1000px;">
          <el-table-column
            prop="id"
            label="序号"
            width="100">
          </el-table-column>
          <el-table-column
            prop="goodsCover"
            label="物品"
            width="120">
            <template v-slot="scope">
              <el-image v-if="scope.row.goodsCover"
                        :src="scope.row.goodsCover"
                        class="avatar"/>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              <el-tag type="message">{{scope.row.goodsName}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="goodsName"
            label="名称"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="brand"
            label="品牌"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="category.label"
            label="分类"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="amounts"
            label="数量"
            width="150"
          >
          </el-table-column>
          <el-table-column
            label="操作"
            v-if="onoff"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleItemPush(scope.row)"
              >选择此物品
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openTwo=false">关闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listBill, getBill, delBill, addBill, updateBill, cancellation} from "@/api/store/bill";
import {listBillItem, getBillItem, delBillItem, addBillItem, updateBillItem} from "@/api/store/billItem";
import {listAllStore} from "@/api/workflow/warehouse"
import {listGoods} from "@/api/store/goods";
import {createObject} from "@/utils";
import goods from "@/views/store/goods";
import Goods from "@/views/store/goods";
export default {
  name: "Bill",
  components: {Goods},
  dicts: ['sb_type', 'sb_status'],
  comments:{
    goods:goods
  },
  data() {
    return {
      goodsList:null,
      goodsParams: {
        pageNum: 1,
        pageSize: 10,
        goodsName: null,
        brand: null,
        spec: null,
        goodsDesc: null,
        params:{}
      },
      openTwo:false,
      disabled: false,
      storeList: [],
      onoff: true,
      itemData: [],
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
      // 出入库单据表格数据
      billList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeBusiDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
        storeId: null,
        busiDate: null,
        status: null,
      },
      // 表单参数
      form: {
        itemFrom: [],
        storeId: null
      },
      //时间校验(不超过当天的日期)
      expireTimeOption:{
        disabledDate(time){
          return time.getTime() > Date.now() - 8.64e6;
        }
      },
      // 表单校验
      rules: {
        busiDate: [
          {required: true, message: "时间不能超过当天日期", trigger: "blur"}
        ],
        storeId: [
          {required: true, message: "仓库不能为空", trigger: "blur"}
        ],
      },
      cache:{
        node:new Map()
      }
    };
  },

  created() {
    this.getList();
    listAllStore().then(res => {
      console.log("ww", res);
      this.storeList = res.rows
    })
    this.getAllGoods()
  },
  methods: {
    handleItemPush(node){
      if (node.amounts<=0){
        this.$message.warning(`${node.goodsName}没有库存了，请添加物品库存`)
      }
      const nodes = this.form.itemFrom.filter(item=>item.id===node.id);
      if (nodes.length!==0){
        this.$message.warning(`${node.goodsName}已存在，请勿重新添加`)
        return
      }
      //检查通过后准许进入
      const object = createObject(node);
      object.sum=0;
      object.amounts=0;
      this.form.itemFrom.push(object)

    },
    async getAllGoods(){
      const {rows}=await listGoods(this.goodsParams);
      rows.map(row=>{
        return {
          ...row,
          flag:false
        }
      })
      this.goodsList=rows
    },
    validateField(form, index) {
      let result = true;
      for (let item of this.$refs[form].fields) {
        if (item.prop.split(".")[1] == index) {
          this.$refs[form].validateField(item.prop, err => {
            if (err != "") {
              result = false;
            }
          });
        }
        if (!result) break;
      }
      return result;
    },
    handleAddGoods(){
      this.openTwo=true
      this.title = "添加商品";
    },
    numChange(row) {
      row.sum = row.price * row.amounts
    },
    /** 查询出入库单据列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeBusiDate && '' != this.daterangeBusiDate) {
        this.queryParams.params["beginBusiDate"] = this.daterangeBusiDate[0];
        this.queryParams.params["endBusiDate"] = this.daterangeBusiDate[1];
      }
      listBill(this.queryParams).then(response => {
        this.billList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    //删除明细
    async itemDelete(row) {
     const {msg} =await delBillItem(row.id);
     const itemForm = this.form.itemFrom.filter(v=>v.id!==msg);
     this.form.itemFrom=itemForm;

      console.log(data)
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelGoods(){
      this.openTwo=false
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        type: null,
        storeId: null,
        busiDate: null,
        status: null,
        operateDate: null,
        operatorId: null,
        remark: null,
        itemFrom: []
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
      this.daterangeBusiDate = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      if (selection.length == 1 && selection[0].status == 0 && selection[0].type == 0) {
        this.form = {...selection[0]}
      } else {
        this.form = {}
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.disabled = false
      this.open = true;
      this.onoff = true
      this.title = "添加出入库单据";
    },

    /** 修改按钮操作 */
     handleUpdate() {
        this.onoff = true
        this.open = true;
        this.title = "修改出入库单据";
    },
    async itemQuery(id) {
      this.reset();
      await getBillItem(id).then(res => {
        let {data} = res
        data = data.map(s => {
          return {
            ...s,
            sum: s.price * s.amounts
          }
        })
        this.form.itemFrom = data
      })
      await getBill(id).then(response => {
        this.form.busiDate = response.data.busiDate;
        this.form.remark = response.data.remark;
        this.form.storeId = response.data.storeId
        this.form.id = response.data.id
        this.onoff = false
        this.open = true
        this.disabled = true
        this.title = "修改出入库单据";
      });
    }
    ,
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        console.log(this.form)
        if (valid) {
          if (this.form.id != null) {
            updateBill(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBill(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
    ,
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除出入库单据编号为"' + ids + '"的数据项？').then(function () {
        return cancellation(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    }
    ,
    /** 导出按钮操作 */
    handleExport() {
      this.download('store/bill/export', {
        ...this.queryParams
      }, `bill_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<!--<style type="text/css">-->
<!-- .a{-->
<!--   width: 50px;-->
<!-- }-->
<!--</style>-->
