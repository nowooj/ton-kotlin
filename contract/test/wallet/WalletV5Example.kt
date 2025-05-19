package org.ton.contract.wallet

import io.github.andreypfau.kotlinx.crypto.sha2.sha256
import kotlinx.coroutines.runBlocking
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.AddrStd
import org.ton.block.Coins
import org.ton.block.CurrencyCollection
import org.ton.block.ExtraCurrencyCollection
import kotlin.io.encoding.Base64
import kotlin.test.Test

class WalletV5Example {
    @Test
    fun walletV5Example(): Unit = runBlocking {
        val liteClient = liteClientTestnet()

        val pk = PrivateKeyEd25519(sha256("example-key".encodeToByteArray()))

        val walletID = WalletId(0, 0, -3, 0)
        val contract = WalletV5R1Contract(
            liteClient,
            WalletV5R1Contract.address(pk, walletID),
            walletID,
        )

        // Query Get /api/v2/getWalletInformation?address=
        // https://testnet.toncenter.com/api/v2/getWalletInformation?address=0QB27Zq_7tkXj8_dZV70VX1zUkxvbF5x9-1lbsDxX3SzHcfm
        val seqno = 10

        val cell = contract.transferMsg(
            pk,
            walletID,
            seqno,
            WalletTransfer(
                AddrStd("kf8ZzXwnCm23GeqkK8ekU0Dxzu_fiXqIYO48FElkd7rVnoix"),                    // to address
                true,                                                                           // bounceable
                CurrencyCollection(Coins.Companion.ofNano(100), ExtraCurrencyCollection.EMPTY), // amount
                3,                                                                              // SendMode
                MessageData.text("Hello, World!")))                                             // memo

        // POST /api/v2/sendBocReturnHash
        println(Base64.encode(cell.toByteArray()))
    }
}
