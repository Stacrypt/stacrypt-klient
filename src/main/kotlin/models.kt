package io.stacrypt.stadroid.data

//import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//@Entity
data class Market(
    /*/*@PrimaryKey */*/var name: String,
                        var stock: String,
                        var stockPrec: Int,
                        var money: String,
                        var feePrec: Int,
                        var minAmount: String,
                        var moneyPrec: Int,
                        var defaultDepthInterval: String?,
                        var priceStep: BigDecimal?,
                        var amountStep: BigDecimal?,
                        var quoteMin: BigDecimal?,
                        var quoteMax: BigDecimal?
//    @Embedded var status: MarketStatus?,
//    @Embedded var summary: MarketSummary?,
//    @Embedded var last: MarketLast?

)

data class MarketStatus(
    val openStatus: String,
    val highStatus: String,
    val lowStatus: String,
    val closeStatus: String,
    val lastStatus: String,
    val dealStatus: String,
    val volumeStatus: String,
    val periodStatus: String
)

data class MarketSummary(
    val open24: String,
    val last24: String,
    val high24: String,
    val low24: String,
    val deal24: String,
    val volume24: String
)

data class MarketLast(
    val price: String
)

//@Entity
data class Asset(
    /*@PrimaryKey */var name: String,
                    var prec: String
)

//@Entity
data class Currency(
    var name: String,
    /*@PrimaryKey */var symbol: String,
    var type: String, // fiat / cryptocurrency
    val normalizationScale: Int,
    val smallestUnitScale: Int,
    var swapMin: String?,
    var swapMax: String?,
    var swapStep: String?,

    /**
     * Just for crypto:
     */
    var depositMin: String?,
    var depositMax: String?,
    var withdrawMin: String?,
    var withdrawMax: String?,
    var depositMaxCommission: String?,
    var withdrawMaxCommission: String?,
    var depositStaticCommission: String?,
    var withdrawStaticCommission: String?,
    var depositCommissionRate: String?,
    var withdrawCommissionRate: String?,
    var supportAddressRenewal: Boolean?,
    var supportNetworkFee: Boolean?,
    var walletId: String?

)

data class Order(
    val id: Int,
    val createdAt: Date?,
    val modifiedAt: Date?,
    val finishedAt: Date?,
    val market: String,
    val user: Int,
    val type: String,
    val side: String,
    val amount: String,
    val price: String?,
    val takerFeeRate: String?,
    val makerFeeRate: String?,
    val source: String?,
    val filledMoney: String?,
    val filledStock: String?,
    val filledFee: String?
)

data class Kline(
    var market: String,
    var time: Long,
    var o: String,
    var h: String,
    var l: String,
    var c: String,
    var volume: String,
    var amount: String
)

data class Book(
    var market: String,
    var i: Int,
    var side: String,
    var price: String,
    var amount: String
)

data class Depth(
    var asks: ArrayList<DepthRecord>,
    var bids: ArrayList<DepthRecord>
)

data class DepthRecord(
    var price: BigDecimal,
    var amount: BigDecimal
)

data class Deal(
    var market: String,
    var id: Int,
    var time: Date,
    var type: String,
    var amount: String,
    var price: String
)

data class Mine(
    var id: Int,
    var market: String,
    var user: String,
    var time: Date,
    var side: String,
    var role: String,
    var amount: String,
    var price: String,
    var fee: String,
    var fillId: Int,
    var orderId: Int
)

//@Entity
data class BalanceOverview(
    /*@PrimaryKey */@SerializedName("name") var assetName: String,
                    var available: String,
                    var freeze: String
)

data class BalanceHistory(
    @SerializedName("time") var time: Date,
    @SerializedName("asset") var assetName: String,
    @SerializedName("business") var business: String,
    @SerializedName("change") var change: String,
    @SerializedName("balance") var balance: String,
    @SerializedName("detail") var detail: String
)

data class DepositInfo(
    @SerializedName("id") var id: String,
    @SerializedName("user") var user: String,
    @SerializedName("extra") var extra: String?,
    @SerializedName("creation") var creation: Date,
    @SerializedName("expiration") var expiration: Date?,
    @SerializedName("address") var address: String
)

//@Entity
data class User(
    val id: Int,
    /*@PrimaryKey */val email: String,
    val type: String,
    val isEmailVerified: Boolean,
    val isEvidenceVerified: Boolean,
    val hasSecondFactor: Boolean,
    val isActive: Boolean,
    val invitationCode: String?,
    val createdAt: Date?,
    val modifiedAt: Date?
)

data class TokenResponse(
    val token: String
)

enum class Gender { MALE, FEMALE }

data class Evidence(
    val clientId: Int,
    val cityId: Int?,
    val type: String,
    val gender: Gender?,
    val birthday: Date?,
    val mobilePhone: String?,
    val fixedPhone: String?,
    val address: String?,
    val createdAt: Date?,
    val modifiedAt: Date?,
    val firstName: String?,
    val lastName: String?,
    val nationalCode: String?,
    val error: String?,
    val idCard: String?,
    val idCardSecondary: String?
)

//@Entity
data class Ticket(
    /*@PrimaryKey */var id: Int,
                    var title: String,
                    var memberId: Int?,
                    var closedAt: Date?,
    /*@Embedded(prefix = "dp") */var department: TicketDepartment,
                    var isClosed: Boolean,
    /*@Embedded(prefix = "fm") */var firstMessage: TicketMessage?,
                    var createdAt: Date?,
                    var modifiedAt: Date?
)

//@Entity(
//    primaryKeys = ["id"],
//    foreignKeys = [
//        ForeignKey(
//            entity = Ticket::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("ticketId"),
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
data class TicketMessage(
    var id: Int,
    var text: String,
    var isAnswer: Boolean,
    var ticketId: Int,
    var attachment: String?,
    var memberId: Int?,
    var createdAt: Date?,
    var modifiedAt: Date?
)

data class TicketDepartment(
    var id: Int,
    var title: String
)

data class Session(
    var id: String,
    var remoteAddress: String?,
    var machine: String?,
    var os: String?,
    var agent: String?,
    var client: String?,
    var app: String?,
    var lastActivity: Date?
)

data class SecurityLog(
    var id: Int,
    var clientId: Int?,
    var type: String?,
    var details: String?,
    var createdAt: Date?
)

data class ErrorResponse(
    val message: String,
    val description: String
)


enum class BankIdType { ACCOUNT, CARD }

interface BankId {
    val id: Int
    val clientId: Int
    val isVerified: Int
    val error: Int
    val fiatSymbol: String
    val type: BankIdType
}

data class BankAccount(
    override val id: Int,
    override val clientId: Int,
    override val isVerified: Int,
    override val error: Int,
    override val fiatSymbol: String,
    override val type: BankIdType = BankIdType.ACCOUNT,
    val iban: String,
    val owner: String,
    val bic: String
) : BankId

data class BankCard(
    override val id: Int,
    override val clientId: Int,
    override val isVerified: Int,
    override val error: Int,
    override val fiatSymbol: String,
    override val type: BankIdType = BankIdType.CARD,
    val pan: String,
    val holder: String
) : BankId

//@Entity
data class PaymentGateway(
    /*@PrimaryKey */val name: String,
                    val cashinMin: String,
                    val cashoutStaticCommission: String,
                    val cashinMax: String,
                    val cashoutMax: String,
                    val cashinMaxCommission: String,
                    val cashinStaticCommission: String,
                    val cashinCommissionRate: String,
                    val cashoutMaxCommission: String,
                    val cashoutCommissionRate: String,
                    val cashoutMin: String,
                    val fiatSymbol: String,
    /*@Embedded(prefix = "fiat_") */val fiat: Fiat
)

data class Fiat(
    val name: String,
    val type: String,
    val symbol: String,
    val normalizationScale: Int,
    val smallestUnitScale: Int
)

data class Price(
    var id: Int,
    var currencySymbol: String,
//    var currency: Currency?,
    var criterionCurrencySymbol: String,
//    var criterionCurrency: Currency?,
    var ticker: BigDecimal,
    var swing: BigDecimal,
    var createdAt: Date
)

val dateFormatter by lazy { SimpleDateFormat("dd MMM yyyy HH:mm") }
fun Date.format() = dateFormatter.format(this)