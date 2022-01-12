TOP_TARGETS = build clean deploy undeploy
COMPONENTS = client resource-server auth-server

$(TOP_TARGETS): $(COMPONENTS)
$(COMPONENTS):
	@echo "Making $@::$(MAKECMDGOALS)..."
	$(MAKE) -C $@ $(MAKECMDGOALS)

.PHONY: $(TOPTARGETS) $(COMPONENTS)