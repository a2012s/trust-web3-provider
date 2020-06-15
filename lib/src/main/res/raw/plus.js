(function() {
 console.log(999);
      var config = {
          address: "0xEa5CDC6625416c87913180847ace2Bf42Dbb422e".toLowerCase(),
          chainId: 1,
          rpcUrl: "https://mainnet.infura.io/v3/76208b0279d342f1aa8857cb5a31e45e"
      };
      const provider = new window.Trust(config);
      window.ethereum = provider;
      window.web3 = new window.Web3(provider);
      window.web3.eth.defaultAccount = config.address;

      window.chrome = {webstore: {}};
       console.log(1000);
  })();