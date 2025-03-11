package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.model.Role;
import UTCN_IMDB.demo.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
public class RoleController
{
    private final RoleService roleService;

    @GetMapping("/role")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/role")
    public Role addRole(@RequestBody Role role) {
        return roleService.addRole(role);
    }

    @PutMapping("/role/{uuid}")
    public Role updateRole(@PathVariable UUID uuid, @RequestBody Role role) {
        return roleService.updateRole(uuid, role);
    }

    @DeleteMapping("/role/{uuid}")
    public void deleteRole(@PathVariable UUID uuid) {
        roleService.deleteRole(uuid);
    }

    @GetMapping("/role/roleName/{roleName}")
    public Role getRoleByRoleName(@PathVariable String roleName) {
        return roleService.getRoleByRoleName(roleName);
    }

    @GetMapping("/role/{uuid}")
    public Role getRoleById(@PathVariable UUID uuid) {
        return roleService.getRoleById(uuid);
    }
}
