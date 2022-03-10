# FlashPipe
Experimental Repo to test the usage of FlashPipe with GitHub actions. The main idea is to add on-the-fly commits from the SAP with UI. 

The two pipelines are still manually triggered on GitHub. The only advantage is the IF doesn't need to be manually downloaded and uploaded to the Git repository.

## Pipelines

Basic Authentication is used. Three credentials are required in the security settings:
-DEV_HOST_TMN: The link to the tenant, without https:// Example: \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*.hana.ondemand.com/
-DEV_USER: your P User
-DEV_PASSWORD

**Never store these as plain text!**

### Sync
The included IFlows get updated in the repo from the tenant. When you run the workflow it will ask for some information:

Package ID
Included Ids (optional) (leave blank to sync any IF)
Excluded Ids (optional) (leave blank to sync any IF)
Draft handling (optional) - SKIP, ADD, ERROR (leave blank to SKIP)
For now, you have to enter the values manually. The idea is to automate this process later.

Should you edit the repository from GitHub and then run the sync, the git repo will be reverted to the last version of the tenant.


### Build
The IF found in the git repository is uploaded or updated in design time. Then it gets deployed to runtime. 

These workflow is also triggered manually, and will ask for some information:

Package ID
Package Name
The ID of the IF
The name of the IF
For now, you have to enter the values manually. The idea is to automate this process later.

Should you edit the repository from GitHub and then run the build, the tenant will be updated to the last version of the git repository.
